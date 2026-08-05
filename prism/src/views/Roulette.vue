<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import BreathingBackground from '@components/BreathingBackground.vue'
import ProfileDropdown from '@components/ProfileDropdown.vue'
import RouletteWheel from '@components/roulette/RouletteWheel.vue'
import BetPanel from '@components/roulette/BetPanel.vue'
import { useAuthStore } from '@/stores/auth'
import { getUserBalance } from '@/services/userService'
import { spinRoulette } from '@/services/rouletteService'
import { RESULT_COLORS } from '@/constants/roulette'
import '@/assets/css/fonts.css'

const router = useRouter()
const authStore = useAuthStore()

const wheelRef = ref(null)
const balance = ref(0)
const betAmount = ref(100)
const spinning = ref(false)
const resultText = ref('Place your bet!')
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

const handleSpin = async (betType) => {
  if (spinning.value) return

  spinning.value = true
  resultText.value = 'Spinning...'
  resultColor.value = '#f2f3f5'
  // Start motion immediately; API runs in parallel so latency isn't felt as dead time.
  wheelRef.value?.beginSpin()

  try {
    const data = await spinRoulette({
      betType,
      betAmount: betAmount.value,
    })

    await wheelRef.value?.spinTo(data.resultNumber)

    balance.value = data.newBalance

    if (data.won) {
      resultText.value = `${data.resultColor} - You Won ${data.payout} 🪙`
      resultColor.value = RESULT_COLORS.WIN
    } else {
      resultText.value = `${data.resultColor} - You Lost`
      resultColor.value = RESULT_COLORS.LOSE
    }
  } catch (error) {
    wheelRef.value?.cancelSpin()
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
  <div class="roulette-page">
    <nav class="top-nav">
      <h1 class="brand-logo" @click="goHome">teaz.fun</h1>
      <div class="nav-right">
        <button type="button" class="back-home" @click="goHome">Back home ↵</button>
        <ProfileDropdown :username="authStore.username" :avatarUrl="authStore.avatarUrl" />
      </div>
    </nav>

    <main class="roulette-wrapper">
      <div class="result-msg" :style="resultStyle">{{ resultText }}</div>

      <RouletteWheel ref="wheelRef" />

      <div class="balance-line">Balance: {{ balance }} 🪙</div>

      <BetPanel v-model="betAmount" :disabled="spinning" @spin="handleSpin" />
    </main>
  </div>
</template>

<style scoped>
.roulette-page {
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

.roulette-wrapper {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 30px;
  padding: 20px;
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
</style>
