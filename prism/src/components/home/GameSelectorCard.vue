<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import Card from '@components/Card.vue'
import Button from '@components/Button.vue'

const router = useRouter()

// Reactive data source. Probably best to move this into a configuration file.
const games = ref([
  {
    id: 'slots',
    title: 'Slots',
    subtitle: 'Win big',
    icon: '🎰',
    route: '/games/slots',
  },
  {
    id: 'roulette',
    title: 'Roulette',
    subtitle: 'Spin the wheel',
    icon: '🔴',
    route: '/games/roulette',
  },
  {
    id: 'horserace',
    title: 'Horse Racing',
    subtitle: 'Global Room',
    icon: '🐎',
    route: '/minigames/race/global',
  },
])

const playGame = (route) => {
  router.push(route)
}
</script>

<template>
  <Card title="Minigames" class="games-card">
    <div class="game-list">
      <div v-for="game in games" :key="game.id" class="game-item">
        <div class="game-icon">{{ game.icon }}</div>

        <div class="game-info">
          <h4>{{ game.title }}</h4>
          <span>{{ game.subtitle }}</span>
        </div>

        <Button color="primary" class="play-btn" @click="playGame(game.route)"> Play </Button>
      </div>
    </div>
  </Card>
</template>

<style scoped>
.games-card {
  display: flex;
  flex-direction: column;
}

.game-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 15px;
}

@media (max-width: 600px) {
  .game-list {
    grid-template-columns: 1fr;
  }
}

.game-item {
  background: #2b2d31;
  padding: 15px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 15px;
  transition:
    transform 0.2s,
    background 0.2s;
}

.game-item:hover {
  transform: translateY(-3px);
  background: #313338;
}

.game-icon {
  font-size: 1.5rem;
  background: #1e1f22;
  width: 50px;
  height: 50px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.game-info {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
}

.game-info h4 {
  margin: 0;
  font-size: 1rem;
  color: #f2f3f5;
}

.game-info span {
  font-size: 0.8rem;
  color: #949ba4;
}

.play-btn {
  margin-top: 0 !important;
  padding: 8px 20px;
}
</style>
