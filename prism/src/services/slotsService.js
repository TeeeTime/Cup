import { apiFetch } from './api'

export const spinSlots = async ({ betAmount }) => {
  try {
    const response = await apiFetch('/api/slots/spin', {
      method: 'POST',
      body: JSON.stringify({ betAmount }),
    })

    if (!response.ok) {
      throw new Error(`Backend returned status: ${response.status}`)
    }

    return await response.json()
  } catch (error) {
    console.error('SlotsService Error:', error)
    throw error
  }
}
