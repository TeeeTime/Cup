import { apiFetch } from './api'

export const getUserBalance = async (userId) => {
  try {
    const response = await apiFetch(`http://localhost:8080/api/users/${userId}/balance`, {
      method: 'GET',
    })

    if (!response.ok) {
      throw new Error(`Backend returned status: ${response.status}`)
    }

    return await response.json()
  } catch (error) {
    console.error('UserService Error:', error)
    throw error
  }
}
