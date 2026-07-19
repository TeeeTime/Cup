import { apiFetch } from './api'

export const getLeaderboard = async () => {
  try {
    const response = await apiFetch('/api/leaderboard', {
      method: 'GET'
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
