<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'

const props = defineProps({
  username: {
    type: String,
    default: 'User'
  },
  avatarUrl: {
    type: String,
    default: 'https://cdn.discordapp.com/embed/avatars/0.png'
  }
})

const router = useRouter()
const authStore = useAuthStore()

const isOpen = ref(false)
const dropdownRef = ref(null)

const toggleDropdown = () => {
  isOpen.value = !isOpen.value
}

// Closes the dropdown if you click anywhere outside of it
const closeOnClickOutside = (event) => {
  if (dropdownRef.value && !dropdownRef.value.contains(event.target)) {
    isOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', closeOnClickOutside)
})

onUnmounted(() => {
  document.removeEventListener('click', closeOnClickOutside)
})

const handleLogout = () => {
  authStore.logout()
  router.push('/login')
}
</script>

<template>
  <div class="profile-dropdown" ref="dropdownRef">

    <button class="profile-trigger" @click="toggleDropdown" :class="{ 'is-active': isOpen }">
      <img :src="avatarUrl" class="nav-avatar" alt="Profile Avatar" />
      <span class="nav-username">{{ username }}</span>
      <svg class="arrow" :class="{ 'rotate': isOpen }" width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <polyline points="6 9 12 15 18 9"></polyline>
      </svg>
    </button>

    <transition name="fade-slide">
      <div v-if="isOpen" class="dropdown-content">

        <router-link to="/settings" class="dropdown-item" @click="isOpen = false">
          <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="12" cy="12" r="3"></circle>
            <path d="M19.4 15a1.65 1.65 0 0 0 .33 1.82l.06.06a2 2 0 0 1 0 2.83 2 2 0 0 1-2.83 0l-.06-.06a1.65 1.65 0 0 0-1.82-.33 1.65 1.65 0 0 0-1 1.51V21a2 2 0 0 1-2 2 2 2 0 0 1-2-2v-.09A1.65 1.65 0 0 0 9 19.4a1.65 1.65 0 0 0-1.82.33l-.06.06a2 2 0 0 1-2.83 0 2 2 0 0 1 0-2.83l.06-.06a1.65 1.65 0 0 0 .33-1.82 1.65 1.65 0 0 0-1.51-1H3a2 2 0 0 1-2-2 2 2 0 0 1 2-2h.09A1.65 1.65 0 0 0 4.6 9a1.65 1.65 0 0 0-.33-1.82l-.06-.06a2 2 0 0 1 0-2.83 2 2 0 0 1 2.83 0l.06.06a1.65 1.65 0 0 0 1.82.33H9a1.65 1.65 0 0 0 1-1.51V3a2 2 0 0 1 2-2 2 2 0 0 1 2 2v.09a1.65 1.65 0 0 0 1 1.51 1.65 1.65 0 0 0 1.82-.33l.06-.06a2 2 0 0 1 2.83 0 2 2 0 0 1 0 2.83l-.06.06a1.65 1.65 0 0 0-.33 1.82V9a1.65 1.65 0 0 0 1.51 1H21a2 2 0 0 1 2 2 2 2 0 0 1-2 2h-.09a1.65 1.65 0 0 0-1.51 1z"></path>
          </svg>
          Settings
        </router-link>

        <div class="divider"></div>

        <button class="dropdown-item danger" @click="handleLogout">
          <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"></path>
            <polyline points="16 17 21 12 16 7"></polyline>
            <line x1="21" y1="12" x2="9" y2="12"></line>
          </svg>
          Disconnect
        </button>

      </div>
    </transition>
  </div>
</template>

<style scoped>
.profile-dropdown {
  position: relative;
  font-family: 'ggsans', sans-serif;
}

/* Trigger Button */
.profile-trigger {
  display: flex;
  align-items: center;
  gap: 10px;
  background: rgba(43, 45, 49, 0.6);
  backdrop-filter: blur(10px);
  padding: 6px 12px 6px 6px;
  border-radius: 50px;
  border: 1px solid rgba(255, 255, 255, 0.05);
  cursor: pointer;
  color: #dcddde;
  transition: all 0.2s ease;
}

.profile-trigger:hover, .profile-trigger.is-active {
  background: rgba(56, 60, 66, 0.8);
  border-color: rgba(255, 255, 255, 0.1);
}

.nav-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.nav-username {
  font-size: 0.95rem;
  font-weight: 500;
}

.arrow {
  color: #b9bbbe;
  transition: transform 0.3s ease;
}
.arrow.rotate {
  transform: rotate(180deg);
}

/* Dropdown Menu */
.dropdown-content {
  position: absolute;
  right: 0;
  top: 100%;
  margin-top: 8px;
  width: 200px;
  background-color: #111214;
  border: 1px solid #2b2d31;
  border-radius: 8px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
  padding: 8px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  z-index: 50;
}

/* Items */
.dropdown-item {
  display: flex;
  align-items: center;
  gap: 10px;
  width: 100%;
  padding: 10px 12px;
  box-sizing: border-box;
  background: transparent;
  border: none;
  border-radius: 4px;
  color: #b5bac1;
  font-size: 0.95rem;
  font-weight: 500;
  text-align: left;
  text-decoration: none;
  cursor: pointer;
  transition: background 0.15s, color 0.15s;
}

.icon {
  width: 18px;
  height: 18px;
}

/* Muted Hover States */
.dropdown-item:hover {
  background-color: #2b2d31; /* Subtle gray highlight instead of bright blue */
  color: #dbdee1;
}

.dropdown-item.danger:hover {
  background-color: rgba(218, 55, 60, 0.1); /* Very subtle red background */
  color: #da373c; /* Red text to signify danger */
}

.divider {
  height: 1px;
  background-color: #2b2d31;
  margin: 4px 0;
}

/* Vue Transition Animations */
.fade-slide-enter-active,
.fade-slide-leave-active {
  transition: opacity 0.2s ease, transform 0.2s ease;
}

.fade-slide-enter-from,
.fade-slide-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>
