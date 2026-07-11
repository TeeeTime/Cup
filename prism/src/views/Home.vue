<script setup>
import Card from '@components/Card.vue'
import BreathingBackground from '@components/BreathingBackground.vue'
import ProfileDropdown from '@components/ProfileDropdown.vue'
import DailyRewardCard from '@components/home/DailyRewardCard.vue'
import LeaderboardCard from '@components/home/LeaderboardCard.vue'
import '@/assets/css/fonts.css'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getUserBalance } from '../services/userService'
import BalanceCard from '@components/home/BalanceCard.vue'
import GameSelectorCard from '@components/home/GameSelectorCard.vue'

const router = useRouter()
const authStore = useAuthStore()
const coinBalance = ref('...')

const fetchBalance = async () => {
  try {
    const data = await getUserBalance(authStore.userId)
    coinBalance.value = data + '🪙'
  } catch (error) {
    coinBalance.value = 'Error'
    console.error('Failed to load balance on the Home view.')
  }
}

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}

onMounted(() => {
  fetchBalance()
})
</script>

<template>
  <BreathingBackground />
  <div class="home-layout">
    <nav class="top-nav">
      <h1 class="brand-logo">teaz.fun</h1>

      <ProfileDropdown :username="authStore.username" :avatarUrl="authStore.avatarUrl" />
    </nav>

    <main class="card-grid">
      <BalanceCard class="layout-balance" />

      <DailyRewardCard class="layout-daily" />

      <LeaderboardCard class="layout-leaderboard" />

      <GameSelectorCard class="layout-games" />
    </main>
  </div>
</template>

<style scoped>
.home-layout {
  padding: 40px;
  color: white;
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
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: auto auto;
  gap: 25px;
  max-width: 1400px;
  margin: 0 auto;
}

.layout-balance {
  grid-column: 1 / 2;
}

.layout-daily {
  grid-column: 2 / 3;
}

.layout-leaderboard {
  grid-column: 3 / 4;
  grid-row: 1 / span 2;
}

.layout-games {
  grid-column: 1 / 3;
  grid-row: 2 / 3;
}

@media (max-width: 1100px) {
  .card-grid {
    grid-template-columns: 1fr;
    grid-template-rows: auto;
  }

  .layout-balance,
  .layout-daily,
  .layout-leaderboard,
  .layout-games {
    grid-column: 1 / -1;
    grid-row: auto;
  }
}
</style>
