<script setup></script>

<template>
  <div class="not-found">
    <h1>404</h1>
    <p>Page not found</p>
    <router-link to="/">Go home</router-link>
  </div>
</template>

<style scoped>
.not-found {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  gap: 8px;
  color: #dcddde;
  font-family: 'ggsans', sans-serif;
}

h1 {
  margin: 0;
  font-size: 4rem;
  color: #000000;
}

p {
  margin: 0 0 12px;
  color: #949ba4;
}

a {
  color: #5865f2;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}
</style>
