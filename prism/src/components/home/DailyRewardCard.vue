<script setup>
import { onMounted, ref } from 'vue'
import Card from '@components/Card.vue'
import Button from '@components/Button.vue'
import ProgressBar from '@components/ProgressBar.vue'
import { getDailyData, claimDailyBonus } from '@/services/dailyRewardService.js'
import { useAuthStore } from '@/stores/auth.js'

const authStore = useAuthStore()

const emit = defineEmits(['claimed'])

const streak = ref(0)
const streakGoal = ref(7)
const isReady = ref(false)
const cooldownTimeLeft = ref('Loading...')
const isClaiming = ref(false)

const applyDailyData = (data) => {
  streak.value = data.streak
  streakGoal.value = data.streakGoal
  isReady.value = data.ready
  cooldownTimeLeft.value = data.cooldownTimeLeft
}

const fetchDailyRewardData = async () => {
  try {
    const data = await getDailyData(authStore.userId)
    applyDailyData(data)
  } catch (error) {
    console.error('Failed to load data on the DailyRewardCard.')
  }
}

const handleClaim = async () => {
  if (isClaiming.value || !isReady.value) return

  isClaiming.value = true
  try {
    const data = await claimDailyBonus()
    applyDailyData(data)
    emit('claimed')
  } catch (error) {
    console.error('Failed to claim daily bonus.')
  } finally {
    isClaiming.value = false
  }
}

onMounted(() => {
  fetchDailyRewardData()
})
</script>

<template>
  <Card title="Daily Reward" class="daily-card">
    <div class="streak-header">
      <div class="streak-count">
        <span class="fire-icon" :class="{ 'is-active': !isReady }">🔥</span>
        <span class="count-text">
          <span>{{ streak }}</span> Day Streak
        </span>
      </div>
      <span class="streak-goal"
        >Goal: <span>{{ streakGoal }}</span> Days</span
      >
    </div>

    <div class="progress-wrapper">
      <ProgressBar :current="streak" :max="streakGoal" />
    </div>

    <div class="action-area">
      <Button v-if="isReady" color="primary" class="full-width-btn" :disabled="isClaiming" @click="handleClaim">
        {{ isClaiming ? 'Claiming...' : 'Claim Bonus' }}
      </Button>

      <Button v-else color="default" class="full-width-btn disabled-btn" disabled>
        Next in: {{ cooldownTimeLeft }}
      </Button>
    </div>
  </Card>
</template>

<style scoped>
.daily-card {
  display: flex;
  flex-direction: column;
  align-self: start;
}

.streak-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 15px;
}

.streak-count {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 1.1rem;
  font-weight: 700;
  color: white;
}

.fire-icon {
  font-size: 1.3rem;
  opacity: 0.5;
  transition: opacity 0.3s ease;
}

.fire-icon.is-active {
  opacity: 1;
  animation: pulse 2s infinite;
}

.streak-goal {
  font-size: 0.8rem;
  color: #949ba4;
  padding-bottom: 2px;
}

.progress-wrapper {
  margin-bottom: 25px;
}

.action-area {
  margin-top: 5px;
}

.full-width-btn {
  width: 100%;
}

.disabled-btn {
  cursor: not-allowed;
  opacity: 0.7;
}

@keyframes pulse {
  0% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
  100% {
    transform: scale(1);
  }
}
</style>
