<script setup>
import Card from '@components/Card.vue'
import { SYMBOLS, PAYOUTS, SEVEN_INDEX, TWO_SEVENS_PAYOUT } from '@/constants/slots'

const combos = SYMBOLS.map((symbol, i) => ({
  key: i,
  label: `${symbol} ${symbol} ${symbol}`,
  multiplier: `${PAYOUTS[i]}x`,
}))

combos.push({
  key: 'two-sevens',
  label: `Any 2 ${SYMBOLS[SEVEN_INDEX]}`,
  multiplier: `${TWO_SEVENS_PAYOUT}x`,
})
</script>

<template>
  <Card class="payout-card" title="Payouts">
    <div v-for="combo in combos" :key="combo.key" class="payout-item">
      <span>{{ combo.label }}</span>
      <span class="multiplier">{{ combo.multiplier }}</span>
    </div>
  </Card>
</template>

<style scoped>
.payout-card {
  width: 100%;
  max-width: 280px;
}

.payout-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 6px 0;
  color: #b9bbbe;
  font-family: 'ggsans', sans-serif;
}

.payout-item span:first-child {
  font-size: 1.4rem;
  color: white;
}

.multiplier {
  font-size: 1rem;
  font-weight: bold;
  color: white;
}
</style>
