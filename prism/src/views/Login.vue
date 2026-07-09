<script setup>
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { onMounted, ref } from 'vue'
import { loginWithDiscordCode } from '../services/authService'

// Component Imports
import BreathingBackground from '@components/BreathingBackground.vue'
import Card from '@components/Card.vue'
import BaseButton from '@components/Button.vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const isLoading = ref(false)
const errorMessage = ref('')

const DISCORD_CLIENT_ID = '1230248435886854238'
const REDIRECT_URI = encodeURIComponent('http://localhost:5173/login')
const DISCORD_AUTH_URL = `https://discord.com/oauth2/authorize?client_id=${DISCORD_CLIENT_ID}&response_type=code&redirect_uri=${REDIRECT_URI}&scope=identify`

const redirectToDiscord = () => {
  window.location.href = DISCORD_AUTH_URL
}

onMounted(async () => {
  const code = route.query.code

  if (code) {
    isLoading.value = true
    try {
      const data = await loginWithDiscordCode(code)

      authStore.login(data.token, data.userId, data.username, data.avatarUrl)

      router.push('/')
    } catch (error) {
      console.error(error)
      errorMessage.value = "Login failed. Please try again."
      router.replace('/login')
    } finally {
      isLoading.value = false
    }
  }
})
</script>

<template>
  <BreathingBackground />

  <div class="login-wrapper">

    <!-- Using your custom Card. Setting noHover to keep the login screen stationary -->
    <Card :noHover="true" class="login-card">

      <!-- Default State: Login Button -->
      <div v-if="!isLoading" class="content">
        <h1 class="brand-title">teaz.fun</h1>
        <p class="subtitle">Access your dashboard</p>
        <BaseButton color="primary" @click="redirectToDiscord">
          <svg class="discord-icon" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 127.14 96.36">
            <path fill="#ffffff" d="M107.7,8.07A105.15,105.15,0,0,0,81.47,0a72.06,72.06,0,0,0-3.36,6.83A97.68,97.68,0,0,0,49,6.83,72.37,72.37,0,0,0,45.64,0,105.89,105.89,0,0,0,19.39,8.09C2.79,32.65-1.71,56.6.54,80.21h0A105.73,105.73,0,0,0,32.71,96.36,77.7,77.7,0,0,0,39.6,85.25a68.42,68.42,0,0,1-10.85-5.18c.91-.66,1.8-1.34,2.66-2a75.57,75.57,0,0,0,64.32,0c.87.71,1.76,1.39,2.66,2a68.68,68.68,0,0,1-10.87,5.19,77,77,0,0,0,6.89,11.1A105.25,105.25,0,0,0,126.6,80.22h0C129.24,52.84,122.09,29.11,107.7,8.07ZM42.45,65.69C36.18,65.69,31,60,31,53s5-12.74,11.43-12.74S54,46,53.89,53,48.84,65.69,42.45,65.69Zm42.24,0C78.41,65.69,73.31,60,73.31,53s5-12.74,11.43-12.74S96.1,46,96,53,91.08,65.69,84.69,65.69Z"/>
          </svg>
          <span>Login with Discord</span>
        </BaseButton>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </div>

      <div v-else>
        <h2 class="authenticating">Authenticating with Nexus...</h2>
      </div>

    </Card>

  </div>
</template>

<style scoped>
.login-wrapper {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
}

.login-card {
  width: 100%;
  max-width: 450px;
  text-align: center;
}

.content {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 15px 0;
}

.brand-title {
  color: #ffffff;
  font-family: 'DiscordNord', sans-serif;
  font-size: 3.5rem;
  margin: 0 0 10px 0;
  letter-spacing: 2px;
}

.subtitle {
  font-family: 'ggsans', sans-serif;
  color: #99aab5;
  font-size: 1.2rem;
  margin-bottom: 35px;
}

.discord-icon {
  width: 24px;
  height: 24px;
}

.error {
  font-family: 'ggsans', sans-serif;
  color: red;
}

.authenticating {
  font-family: 'ggsans', sans-serif;
  color: white;
}
</style>
