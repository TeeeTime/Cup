<script setup>
import { ref, onBeforeUnmount } from 'vue'
import gsap from 'gsap'
import { WHEEL_ORDER } from '@/constants/roulette'

const IDLE_DEG_PER_SEC = 540
const SEC_PER_REVOLUTION = 360 / IDLE_DEG_PER_SEC
// Minimum travel after the API returns so the slowdown feels natural.
const MIN_DECEL_TRAVEL_DEG = 360 * 3

const wheelEl = ref(null)
const pointerEl = ref(null)

let currentRotation = 0
let activeTween = null

const syncRotationFromDom = () => {
  if (wheelEl.value) {
    currentRotation = Number(gsap.getProperty(wheelEl.value, 'rotation')) || 0
  }
}

const killActiveTween = () => {
  if (activeTween) {
    syncRotationFromDom()
    activeTween.kill()
    activeTween = null
  }
}

/**
 * Steady clockwise-looking spin (negative CSS rotation) at a fixed speed.
 * Uses relative infinite loops so velocity never drifts or eases.
 */
const beginSpin = () => {
  if (!wheelEl.value) return

  killActiveTween()
  gsap.set(wheelEl.value, { rotation: currentRotation })

  activeTween = gsap.to(wheelEl.value, {
    rotation: '-=360',
    duration: SEC_PER_REVOLUTION,
    ease: 'none',
    repeat: -1,
  })
}

/**
 * Pick the next landing rotation past `fromRotation` (more negative) that
 * centers `resultNumber` under the pointer, with enough leftover distance to decelerate.
 */
const resolveLandingRotation = (fromRotation, resultNumber) => {
  const index = WHEEL_ORDER.indexOf(resultNumber)
  if (index < 0) return fromRotation

  const degPerSlice = 360 / 37
  const slotCenter = index * degPerSlice + degPerSlice / 2

  // rotation ≡ -slotCenter (mod 360), continuing in the negative direction
  let revolutions = Math.ceil((-fromRotation - slotCenter) / 360)
  let target = -(revolutions * 360 + slotCenter)

  while (fromRotation - target < MIN_DECEL_TRAVEL_DEG) {
    revolutions += 1
    target = -(revolutions * 360 + slotCenter)
  }

  return target
}

/**
 * Seamlessly decelerate from idle speed onto the result.
 * power1.out has initial velocity 2 * distance / duration — set duration so that
 * equals IDLE_DEG_PER_SEC, so there is no speed jump at the handoff.
 */
const spinTo = (resultNumber) => {
  return new Promise((resolve) => {
    if (!wheelEl.value || WHEEL_ORDER.indexOf(resultNumber) < 0) {
      resolve()
      return
    }

    killActiveTween()

    const startRotation = currentRotation
    const targetRotation = resolveLandingRotation(startRotation, resultNumber)
    const distance = startRotation - targetRotation
    const duration = (2 * distance) / IDLE_DEG_PER_SEC

    currentRotation = targetRotation

    activeTween = gsap.to(wheelEl.value, {
      rotation: targetRotation,
      duration,
      ease: 'power1.out',
      onComplete: () => {
        if (pointerEl.value) {
          gsap.fromTo(
            pointerEl.value,
            { scaleY: 1 },
            {
              scaleY: 1.15,
              duration: 0.12,
              yoyo: true,
              repeat: 1,
              transformOrigin: 'top center',
            },
          )
        }
        activeTween = null
        resolve()
      },
    })
  })
}

/** Stop mid-spin (API error) without a landing reveal. */
const cancelSpin = () => {
  killActiveTween()
  if (wheelEl.value) {
    gsap.set(wheelEl.value, { rotation: currentRotation })
  }
}

onBeforeUnmount(() => {
  killActiveTween()
})

defineExpose({ beginSpin, spinTo, cancelSpin })
</script>

<template>
  <div class="wheel-container">
    <div class="pointer">
      <div ref="pointerEl" class="pointer-tip"></div>
    </div>
    <div class="wheel-border">
      <div ref="wheelEl" class="wheel"></div>
    </div>
  </div>
</template>

<style scoped>
.wheel-container {
  position: relative;
  width: 320px;
  height: 320px;
  border-radius: 50%;
  box-shadow: 0 0 30px rgba(0, 0, 0, 0.5);
  box-sizing: border-box;
}

.pointer {
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  z-index: 10;
  pointer-events: none;
}

.pointer-tip {
  width: 0;
  height: 0;
  border-left: 12px solid transparent;
  border-right: 12px solid transparent;
  border-top: 25px solid white;
  filter: drop-shadow(0 2px 2px rgba(0, 0, 0, 0.5));
}

.wheel-border {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 8px solid #ffffff;
  box-shadow: inset 0 0 20px rgba(0, 0, 0, 0.6);
  overflow: hidden;
  position: relative;
  background: #111;
}

.wheel {
  box-sizing: border-box;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background: conic-gradient(
    #3ba55d 0deg 9.73deg,
    #ed4245 9.73deg 19.46deg,
    #202225 19.46deg 29.19deg,
    #ed4245 29.19deg 38.92deg,
    #202225 38.92deg 48.65deg,
    #ed4245 48.65deg 58.38deg,
    #202225 58.38deg 68.11deg,
    #ed4245 68.11deg 77.84deg,
    #202225 77.84deg 87.57deg,
    #ed4245 87.57deg 97.3deg,
    #202225 97.3deg 107.03deg,
    #ed4245 107.03deg 116.76deg,
    #202225 116.76deg 126.49deg,
    #ed4245 126.49deg 136.22deg,
    #202225 136.22deg 145.95deg,
    #ed4245 145.95deg 155.68deg,
    #202225 155.68deg 165.41deg,
    #ed4245 165.41deg 175.14deg,
    #202225 175.14deg 184.87deg,
    #ed4245 184.87deg 194.6deg,
    #202225 194.6deg 204.33deg,
    #ed4245 204.33deg 214.06deg,
    #202225 214.06deg 223.79deg,
    #ed4245 223.79deg 233.52deg,
    #202225 233.52deg 243.25deg,
    #ed4245 243.25deg 252.98deg,
    #202225 252.98deg 262.71deg,
    #ed4245 262.71deg 272.44deg,
    #202225 272.44deg 282.17deg,
    #ed4245 282.17deg 291.9deg,
    #202225 291.9deg 301.63deg,
    #ed4245 301.63deg 311.36deg,
    #202225 311.36deg 321.09deg,
    #ed4245 321.09deg 330.82deg,
    #202225 330.82deg 340.55deg,
    #ed4245 340.55deg 350.28deg,
    #202225 350.28deg 360deg
  );
}
</style>
