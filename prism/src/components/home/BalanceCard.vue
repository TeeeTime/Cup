<script setup>
import { ref, onMounted } from 'vue'
import Card from '@components/Card.vue'
import { useAuthStore } from '@/stores/auth.js'
import { getUserBalance } from '@/services/userService.js'

const authStore = useAuthStore()
const balanceValue = ref('...')

const fetchBalance = async () => {
  try {
    const data = await getUserBalance(authStore.userId)
    balanceValue.value = data
  } catch {
    balanceValue.value = 'Error'
    console.error('Failed to load balance on the BalanceCard.')
  }
}

defineExpose({
  fetchBalance
})

onMounted(() => {
  fetchBalance()
})
</script>

<template>
  <Card title="Total Balance" class="balance-card">
    <div class="balance-display">
      <span class="balance-amount">{{ balanceValue }}</span>
      <span class="currency-symbol">🪙</span>
    </div>
  </Card>
</template>

<style scoped>
.balance-card {
  /* Restores the sleek gradient from your original layout */
  background: linear-gradient(135deg, #1e1f22 0%, #161719 100%);
  display: flex;
  flex-direction: column;
}

.balance-display {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 10px;
  width: 100%;
  margin-top: 5px;
}

.balance-amount {
  font-family: 'ggsans', sans-serif;
  font-size: 4rem;
  font-weight: 700;
  color: #ffffff;
  white-space: nowrap;
}

.currency-symbol {
  font-size: 4rem;
  line-height: 1;
  flex-shrink: 0;
}
</style>
