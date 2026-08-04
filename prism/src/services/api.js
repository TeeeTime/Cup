import { useAuthStore } from '../stores/auth'
import router from '../router/index'

export const apiFetch = async (url, options = {}) => {
  const authStore = useAuthStore()

  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  }

  if (authStore.token) {
    headers['Authorization'] = `Bearer ${authStore.token}`
  }

  const config = {
    ...options,
    headers,
  }

  const response = await fetch(url, config)

  if (response.status === 401 || response.status === 403) {
    console.warn('Session expired or invalid token. Forcing logout.')

    authStore.logout()

    router.push('/login')

    throw new Error('Session expired')
  }

  return response
}
