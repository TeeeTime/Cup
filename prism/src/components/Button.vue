<script setup>
import { computed } from 'vue'

const props = defineProps({
  // Accepts 'default' (gray), 'primary' (blue), 'danger' (red)
  color: {
    type: String,
    default: 'default',
  }
})

const colorClass = computed(() => {
  switch (props.color) {
    case 'primary': return 'btn-primary'
    case 'danger': return 'btn-danger'
    case 'default':
    default: return 'btn-default'
  }
})
</script>

<template>
  <button :class="['btn', colorClass]">
    <slot />
  </button>
</template>

<style scoped>
.btn {
  color: white;
  font-family: 'ggsans', sans-serif;
  font-size: 1rem;
  font-weight: 500;
  border: none;
  padding: 10px 25px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.2s ease, transform 0.1s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
}

/* The click animation applies to everything */
.btn:active {
  transform: scale(0.96);
}

/* --- Color Variants --- */

/* Default (Dark Grey from your basic button) */
.btn-default {
  background-color: #4E5058;
}
.btn-default:hover {
  background-color: #6D6F78;
}
.btn-default:active {
  background-color: #3b3d42;
}

/* Primary (Discord Blurple from your .btn-login) */
.btn-primary {
  background-color: #5865F2;
}
.btn-primary:hover {
  background-color: #4752C4;
}
.btn-primary:active {
  background-color: #3c45a5;
}

/* Danger (Red from your .btn-logout) */
.btn-danger {
  background-color: #ed4245;
}
.btn-danger:hover {
  background-color: #d83c3e;
}
.btn-danger:active {
  background-color: #c03537;
}
</style>
