export const getUserBalance = async (userId) => {
  try {
    const response = await fetch(`http://localhost:8080/api/users/${userId}/balance`)

    if (!response.ok) {
      throw new Error(`Backend returned status: ${response.status}`)
    }

    // Return the actual data back to whatever Vue component called this function
    return await response.json()

  } catch (error) {
    console.error("UserService Error:", error)
    throw error // Re-throw the error so the Vue component knows it failed
  }
}
