import { apiFetch } from './api'

export const loginWithDiscordCode = async (code) => {
  const response = await apiFetch('/api/auth/discord', {
    method: 'POST',
    body: JSON.stringify({ code }),
  })

  if (!response.ok) {
    throw new Error(`Authentication failed with status: ${response.status}`)
  }

  return await response.json()
}
