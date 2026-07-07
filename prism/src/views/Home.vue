<script setup>
import Card from '@components/Card.vue'
import BreathingBackground from '@components/BreathingBackground.vue'
import '@/assets/css/fonts.css'
import { ref, onMounted } from 'vue'
import { getUserBalance } from '../services/userService'

const coinBalance = ref('...')

const fetchBalance = async () => {
  try {
    const data = await getUserBalance('359013020057206786')
    coinBalance.value = data + '🪙'
  } catch (error) {
    coinBalance.value = 'Error'
    console.error("Failed to load balance on the Home view.")
  }
}

onMounted(() => {
  fetchBalance()
})
</script>

<template>
  <BreathingBackground />
  <div class="home-layout">
    <header class="page-header">
      <h1>teaz.fun</h1>
    </header>

    <main class="card-grid">
      <Card title="Balance">
        <p class="balance"><strong>{{ coinBalance }}</strong></p>
      </Card>

      <Card title="Static Stats" :noHover="true">
        <p>This card will not float up when you hover over it.</p>
      </Card>

      <Card>
        <h2>Big Header inside the Slot</h2>
        <p>
          Because there is no title prop provided, the h3 tag in the Card component completely
          vanishes thanks to your v-if statement.
        </p>
      </Card>
    </main>
  </div>
</template>

<style scoped>
.home-layout {
  padding: 40px;
  color: white; /* Ensures text is visible against your dark background */
}

.page-header {
  font-family: 'discordnord', sans-serif;
  margin-top: -30px;
  margin-bottom: 30px;
}

.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 25px;
}

.balance {
  font-family: 'ggsans', sans-serif;
  font-size: 4rem;
  font-weight: bold;
}
</style>
