<script setup></script>

<template>
  <div class="breathing-bg"></div>
</template>

<style scoped>
.breathing-bg {
  position: fixed;
  inset: 0;
  z-index: -1;
  pointer-events: none;
  overflow: hidden;
  background-color: #111214;
}

.breathing-bg::before {
  content: '';
  position: absolute;
  inset: 0;
  background: radial-gradient(
    circle at 50% -20%,
    rgba(255, 255, 255, 0.12) 0%,
    rgba(17, 18, 20, 0) 60%
  );
  animation: breathe 6s ease-in-out infinite;
}

@keyframes breathe {
  0% {
    transform: scale(1);
    opacity: 0.8;
  }
  50% {
    transform: scale(1.2);
    opacity: 1;
  }
  100% {
    transform: scale(1);
    opacity: 0.8;
  }
}
</style>
