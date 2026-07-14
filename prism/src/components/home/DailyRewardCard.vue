<script setup>
import { ref } from 'vue'
import Card from '@components/Card.vue'
import Button from '@components/Button.vue'
import ProgressBar from '@components/ProgressBar.vue'

// Mock data (fetching to be implemented)
const currentStreak = ref(3)
const streakGoal = ref(7)
const dailyReady = ref(true)
const dailyTimeLeft = ref('00h 00m')

// Mock claim (claim call to be implemented)
const handleClaim = () => {
  if (!dailyReady.value) return

  dailyReady.value = false
  currentStreak.value += 1
  dailyTimeLeft.value = '23h 59m'
}
</script>

<template>
  <Card title="Daily Reward" class="daily-card">
    <div class="streak-header">
      <div class="streak-count">
        <span class="fire-icon" :class="{ 'is-active': !dailyReady }">🔥</span>
        <span class="count-text">
          <span>{{ currentStreak }}</span> Day Streak
        </span>
      </div>
      <span class="streak-goal"
        >Goal: <span>{{ streakGoal }}</span> Days</span
      >
    </div>

    <div class="progress-wrapper">
      <ProgressBar :current="currentStreak" :max="streakGoal" />
    </div>

    <div class="action-area">
      <Button v-if="dailyReady" color="primary" class="full-width-btn" @click="handleClaim">
        Claim Bonus
      </Button>

      <Button v-else color="default" class="full-width-btn disabled-btn" disabled>
        Next in: {{ dailyTimeLeft }}
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
