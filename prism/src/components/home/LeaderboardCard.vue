<script setup>
import { ref, onMounted } from 'vue'
import Card from '@components/Card.vue'
import {getLeaderboard} from "@/services/leaderboardService.js";

const leaderboard = ref([])

const fetchLeaderboard = async () => {
  try {
    const data = await getLeaderboard()
    leaderboard.value = data
  } catch (error) {
    data.value = 'Error'
    console.error('Failed to load leaderboard on the LeaderboardCard.')
  }
}

onMounted(() => {
  fetchLeaderboard()
})
</script>

<template>
  <Card title="🏆 Leaderboard" class="leaderboard-card">
    <div class="leaderboard-list">
      <div v-if="leaderboard.length === 0" class="empty-state">No players found yet.</div>

      <div v-else v-for="player in leaderboard" :key="player.rank" class="lb-item">
        <span class="lb-rank" :style="player.rank === 1 ? { color: 'gold' } : {}">
          #{{ player.rank }}
        </span>

        <img :src="player.avatarUrl" class="lb-avatar" alt="Avatar" />

        <div class="lb-info">
          <span class="lb-name">{{ player.name }}</span>
          <span class="lb-score">{{ player.balance.toLocaleString() }}🪙</span>
        </div>
      </div>
    </div>
  </Card>
</template>

<style scoped>
.leaderboard-card {
  display: flex;
  flex-direction: column;
  max-height: 600px;
}

.leaderboard-list {
  max-height: 400px;
  overflow-y: auto;
  padding-right: 5px;

  scrollbar-width: thin;
  scrollbar-color: #4f545c transparent;
}

.leaderboard-list::-webkit-scrollbar {
  width: 6px;
}
.leaderboard-list::-webkit-scrollbar-track {
  background: transparent;
}
.leaderboard-list::-webkit-scrollbar-thumb {
  background: #2b2d31;
  border-radius: 3px;
}

.leaderboard-list::-webkit-scrollbar-thumb:hover {
  background-color: #72767d;
}

.lb-item {
  display: flex;
  align-items: center;
  padding: 10px;
  border-bottom: 1px solid #2b2d31;
  gap: 10px;
}

.lb-item:last-child {
  border-bottom: none;
}

.lb-rank {
  font-weight: bold;
  width: 25px;
}

.lb-avatar {
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

.lb-info {
  display: flex;
  flex-direction: column;
}

.lb-name {
  font-size: 0.9rem;
  font-weight: 600;
  color: white;
}

.lb-score {
  font-size: 0.8rem;
  color: #949ba4;
}

.empty-state {
  padding: 10px;
  color: #999;
  text-align: center;
}
</style>
