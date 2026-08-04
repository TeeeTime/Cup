<script setup>
import '../assets/css/fonts.css'

defineProps({
  title: {
    type: String,
    default: null,
  },
  noHover: {
    type: Boolean,
    default: false,
  },
})
</script>

<template>
  <div class="card" :class="{ 'no-hover': noHover }">
    <h3 v-if="title" class="card-title">{{ title }}</h3>
    <slot />
  </div>
</template>

<style scoped>
.card {
  font-family: 'ggsans', sans-serif;
  background: rgba(30, 31, 35, 0.6);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 24px;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.3);
  padding: 35px;
  position: relative;
  overflow: hidden;
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
}

.card:not(.no-hover):hover {
  transform: translateY(-4px);
  box-shadow: 0 30px 60px rgba(0, 0, 0, 0.4);
}

.card-title {
  color: #f2f3f5;
  font-size: 1.1rem;
  margin-top: 0;
  margin-bottom: 15px;
}
</style>
