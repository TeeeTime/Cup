<script setup>
import Card from '@components/Card.vue'
import BreathingBackground from '@components/BreathingBackground.vue'
import '@/assets/css/fonts.css'
</script>

<template>
  <BreathingBackground />
  <div class="home-layout">
    <header class="page-header">
      <h1>teaz.fun</h1>
    </header>

    <main class="card-grid">
      <Card title="Welcome Bonus">
        <p>This is my test text inside the card! The slot mechanism is working perfectly.</p>
        <button class="test-btn">Claim Now</button>
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

.test-btn {
  margin-top: 15px;
  padding: 10px 20px;
  background: #4ade80;
  border: none;
  border-radius: 8px;
  color: #1e1f23;
  font-weight: bold;
  cursor: pointer;
}
</style>
