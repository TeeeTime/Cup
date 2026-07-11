<script setup>
defineProps({
  current: {
    type: Number,
    required: true,
    default: 0,
  },
  max: {
    type: Number,
    required: true,
    default: 7,
  },
})
</script>

<template>
  <div class="progress-track">
    <div
      v-for="n in max"
      :key="n"
      class="progress-segment"
      :class="{
        active: n <= current,
        'final-active': n === max && n <= current,
      }"
    ></div>
  </div>
</template>

<style scoped>
.progress-track {
  display: flex;
  gap: 4px;
  height: 12px;
  width: 100%;
}

.progress-segment {
  flex-grow: 1;
  background-color: #2b2d31;
  border-radius: 2px;
  transition:
    background-color 0.3s ease,
    box-shadow 0.3s ease;
}

.progress-segment:first-child {
  border-top-left-radius: 6px;
  border-bottom-left-radius: 6px;
}

.progress-segment:last-child {
  border-top-right-radius: 6px;
  border-bottom-right-radius: 6px;
}

.progress-segment.active {
  background-color: #5865f2;
  box-shadow: 0 0 10px rgba(88, 101, 242, 0.4);
}

.progress-segment.final-active {
  background-color: #fee75c;
  box-shadow: 0 0 10px rgba(254, 231, 92, 0.5);
}
</style>
