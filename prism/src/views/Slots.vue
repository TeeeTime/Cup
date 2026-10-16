<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import BreathingBackground from '@components/BreathingBackground.vue'
import ProfileDropdown from '@components/ProfileDropdown.vue'
import SlotMachine from '@components/slots/SlotMachine.vue'
import BetPanel from '@components/slots/BetPanel.vue'
import PayoutCard from '@components/slots/PayoutCard.vue'
import { useAuthStore } from '@/stores/auth'
import { getUserBalance } from '@/services/userService'
import { spinSlots } from '@/services/slotsService'
import { RESULT_COLORS } from '@/constants/slots'
import '@/assets/css/fonts.css'

const router = useRouter()
const authStore = useAuthStore()

const machineRef = ref(null)
const balance = ref(0)
const betAmount = ref(100)
const spinning = ref(false)
const resultText = ref('Ready to Spin!')
const resultColor = ref('#f2f3f5')

const resultStyle = computed(() => ({ color: resultColor.value }))

const fetchBalance = async () => {
  try {
    balance.value = await getUserBalance(authStore.userId)
  } catch (error) {
    console.error(error)
    resultText.value = 'Could not load balance.'
    resultColor.value = RESULT_COLORS.LOSE
  }
}

const goHome = () => {
  router.push('/')
}

const handleSpin = async () => {
  if (spinning.value) return

  spinning.value = true
  resultText.value = 'Spinning...'
  resultColor.value = '#f2f3f5'
  // Start motion immediately; API runs in parallel so latency isn't felt as dead time.
  machineRef.value?.beginSpin()

  try {
    const data = await spinSlots({ betAmount: betAmount.value })

    await machineRef.value?.spinTo(data.symbolIndexes)

    balance.value = data.newBalance

    if (data.won) {
      resultText.value = `YOU WON ${data.payout} 🪙`
      resultColor.value = RESULT_COLORS.WIN
    } else {
      resultText.value = 'Try again'
      resultColor.value = RESULT_COLORS.LOSE
    }
  } catch (error) {
    machineRef.value?.cancelSpin()
    resultText.value = error.message || 'Error connecting to server.'
    resultColor.value = RESULT_COLORS.LOSE
  } finally {
    spinning.value = false
  }
}

onMounted(fetchBalance)
</script>

<template>
  <BreathingBackground />
  <div class="slots-page">
    <nav class="top-nav">
      <h1 class="brand-logo" @click="goHome">teaz.fun</h1>
      <div class="nav-right">
        <button type="button" class="back-home" @click="goHome">Back home ↵</button>
        <ProfileDropdown :username="authStore.username" :avatarUrl="authStore.avatarUrl" />
      </div>
    </nav>

    <main class="slots-content">
      <div class="slots-main">
        <div class="result-msg" :style="resultStyle">{{ resultText }}</div>

        <SlotMachine ref="machineRef" />

        <div class="balance-line">Balance: {{ balance }} 🪙</div>

        <BetPanel v-model="betAmount" :disabled="spinning" @spin="handleSpin" />
      </div>

      <PayoutCard />
    </main>
  </div>
</template>

<style scoped>
.slots-page {
  padding: 40px;
  color: white;
  min-height: 100vh;
  box-sizing: border-box;
}

.top-nav {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: -20px;
  margin-bottom: 30px;
}

.brand-logo {
  font-family: 'discordnord', sans-serif;
  font-size: 2.5rem;
  margin: 0;
  cursor: pointer;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.back-home {
  font-family: 'ggsans', sans-serif;
  background: none;
  border: none;
  color: #b5bac1;
  font-size: 1rem;
  cursor: pointer;
  padding: 0;
  transition: color 0.2s ease;
}

.back-home:hover {
  color: white;
}

.slots-content {
  display: flex;
  justify-content: center;
  align-items: flex-start;
  gap: 30px;
  padding: 20px;
  flex-wrap: wrap;
}

.slots-main {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
}

.result-msg {
  height: 30px;
  font-family: 'ggsans', sans-serif;
  font-size: 1.2rem;
  font-weight: bold;
  text-align: center;
  margin-bottom: 10px;
  transition: color 0.3s ease, opacity 0.3s ease;
}

.balance-line {
  font-family: 'ggsans', sans-serif;
  font-size: 1.1rem;
}

@media (max-width: 1200px) {
  .slots-content {
    flex-direction: column;
    align-items: center;
  }
}
</style>
