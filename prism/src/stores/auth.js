import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('jwt_token') || null)
  const userId = ref(localStorage.getItem('discord_id') || null)
  const username = ref(localStorage.getItem('discord_username') || 'User')
  const avatarUrl = ref(localStorage.getItem('discord_avatar') || 'https://cdn.discordapp.com/embed/avatars/0.png')

  const isLoggedIn = ref(!!token.value)

  function login(newToken, newUserId, newUsername, newAvatarUrl) {
    token.value = newToken
    userId.value = newUserId
    username.value = newUsername
    avatarUrl.value = newAvatarUrl
    isLoggedIn.value = true

    localStorage.setItem('jwt_token', newToken)
    localStorage.setItem('discord_id', newUserId)
    localStorage.setItem('discord_username', newUsername)
    localStorage.setItem('discord_avatar', newAvatarUrl)
  }

  function logout() {
    token.value = null
    userId.value = null
    username.value = 'User'
    avatarUrl.value = 'https://cdn.discordapp.com/embed/avatars/0.png'
    isLoggedIn.value = false

    localStorage.removeItem('jwt_token')
    localStorage.removeItem('discord_id')
    localStorage.removeItem('discord_username')
    localStorage.removeItem('discord_avatar')
  }

  return { token, userId, username, avatarUrl, isLoggedIn, login, logout }
})
