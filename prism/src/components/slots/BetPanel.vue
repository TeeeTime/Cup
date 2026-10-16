<script setup>
import { ref, watch, onMounted, onBeforeUnmount } from 'vue'
import Card from '@components/Card.vue'
import Button from '@components/Button.vue'

const props = defineProps({
  disabled: {
    type: Boolean,
    default: false,
  },
  modelValue: {
    type: Number,
    default: 100,
  },
})

const emit = defineEmits(['update:modelValue', 'spin'])

const betAmount = ref(props.modelValue)

watch(
  () => props.modelValue,
  (value) => {
    betAmount.value = value
  },
)

watch(betAmount, (value) => {
  emit('update:modelValue', value)
})

const clampBet = (value) => {
  const parsed = Number.parseInt(value, 10)
  if (Number.isNaN(parsed) || parsed < 1) {
    return 1
  }
  return parsed
}

const adjustBet = (delta) => {
  betAmount.value = clampBet(betAmount.value + delta)
}

const setBet = (amount) => {
  betAmount.value = clampBet(amount)
}

const doubleBet = () => {
  betAmount.value = clampBet(betAmount.value * 2)
}

const onInputBlur = () => {
  betAmount.value = clampBet(betAmount.value)
}

const spin = () => {
  if (props.disabled) return
  emit('spin')
}

const onKeydown = (event) => {
  if (event.code !== 'Space') return
  event.preventDefault()
  spin()
}

onMounted(() => window.addEventListener('keydown', onKeydown))
onBeforeUnmount(() => window.removeEventListener('keydown', onKeydown))
</script>

<template>
  <Card class="bet-card" no-hover>
    <div class="bet-adjust-row">
      <Button class="btn-adjust" :disabled="disabled" @click="adjustBet(-100)">−</Button>
      <input
        v-model.number="betAmount"
        class="bet-input-display"
        type="number"
        min="1"
        :disabled="disabled"
        @blur="onInputBlur"
      />
      <Button class="btn-adjust" :disabled="disabled" @click="adjustBet(100)">+</Button>
    </div>

    <div class="shortcut-row">
      <Button class="shortcut-btn" :disabled="disabled" @click="setBet(100)">100</Button>
      <Button class="shortcut-btn" :disabled="disabled" @click="setBet(1000)">1k</Button>
      <Button class="shortcut-btn" :disabled="disabled" @click="setBet(10000)">10k</Button>
      <Button class="shortcut-btn" :disabled="disabled" @click="doubleBet">2x</Button>
    </div>

    <button type="button" class="spin-btn" :disabled="disabled" @click="spin">SPIN</button>
    <div class="space-hint">Press <kbd>SPACE</kbd> to spin</div>
  </Card>
</template>

<style scoped>
.bet-card {
  width: 100%;
  max-width: 500px;
  display: flex;
  flex-direction: column;
  gap: 15px;
  padding: 25px;
}

.bet-adjust-row {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 10px;
  height: 45px;
  width: 100%;
}

.btn-adjust {
  height: 100% !important;
  width: 50px !important;
  padding: 0 !important;
  font-size: 1.5rem;
  font-weight: bold;
}

.bet-input-display {
  height: 100%;
  width: 120px;
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 5px;
  box-sizing: border-box;
  color: white;
  font-family: 'ggsans', sans-serif;
  font-size: 1.5rem;
  font-weight: bold;
  text-align: center;
  padding: 0;
  margin: 0;
  outline: none;
  -moz-appearance: textfield;
}

.bet-input-display:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.bet-input-display::-webkit-outer-spin-button,
.bet-input-display::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.shortcut-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  width: 100%;
}

.shortcut-btn {
  width: 100%;
  font-size: 1.2rem;
  padding: 12px 0 !important;
}

.spin-btn {
  width: 100%;
  font-family: 'ggsans', sans-serif;
  font-size: 1.5rem;
  font-weight: 600;
  color: white;
  background-color: #3ba55d;
  border: none;
  padding: 14px 0;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease, transform 0.1s ease;
}

.spin-btn:hover:not(:disabled) {
  background-color: #2d7d46;
}

.spin-btn:active:not(:disabled) {
  transform: scale(0.96);
}

.spin-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.space-hint {
  text-align: center;
  font-family: 'ggsans', sans-serif;
  font-size: 0.85rem;
  color: #949ba4;
}

.space-hint kbd {
  border: 1px solid #4f545c;
  border-radius: 4px;
  padding: 1px 5px;
  font-size: 0.75rem;
  background: #2f3136;
  font-family: 'ggsans', sans-serif;
}
</style>
