import { apiFetch } from './api'

export const spinRoulette = async ({ betType, betAmount }) => {
  try {
    const response = await apiFetch('/api/roulette/spin', {
      method: 'POST',
      body: JSON.stringify({ betType, betAmount }),
    })

    if (!response.ok) {
      throw new Error(`Backend returned status: ${response.status}`)
    }

    return await response.json()
  } catch (error) {
    console.error("LeaderboardService Error:", error)
    throw error
  }
}
