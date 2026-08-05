import { apiFetch } from './api'

export const getDailyData = async (userId) => {
  try {
    const response = await apiFetch(`http://localhost:8080/api/daily/${userId}/status`, {
      method: 'GET',
    })

    if (!response.ok) {
      throw new Error(`Backend returned status: ${response.status}`)
    }

    return await response.json()
  } catch (error) {
    console.error('DailyRewardService Error:', error)
    throw error
  }
}

export const claimDailyBonus = async () => {
  try {
    const response = await apiFetch('http://localhost:8080/api/daily/claim', {
      method: 'POST',
    })

    if (!response.ok) {
      throw new Error(`Backend returned status: ${response.status}`)
    }

    return await response.json()
  } catch (error) {
    console.error('DailyRewardService Error:', error)
    throw error
  }
}
