<script setup>
import Card from '@components/Card.vue'
import BreathingBackground from '@components/BreathingBackground.vue'
import ProfileDropdown from '@components/ProfileDropdown.vue'
import Button from '@components/Button.vue'
import '@/assets/css/fonts.css'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { getUserBalance } from '../services/userService'

const router = useRouter()
const authStore = useAuthStore()
const coinBalance = ref('...')

const fetchBalance = async () => {
  try {
    const data = await getUserBalance(authStore.userId)
    coinBalance.value = data + '🪙'
  } catch (error) {
    coinBalance.value = 'Error'
    console.error("Failed to load balance on the Home view.")
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

      <ProfileDropdown :username="authStore.username" :avatarUrl="authStore.avatarUrl"/>
    </nav>

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
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 25px;
}

.balance {
  font-family: 'ggsans', sans-serif;
  font-size: 4rem;
  font-weight: bold;
}
</style>
