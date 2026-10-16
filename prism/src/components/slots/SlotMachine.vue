<script setup>
import { ref, computed, onBeforeUnmount } from 'vue'
import gsap from 'gsap'
import { SYMBOLS, SEVEN_INDEX } from '@/constants/slots'

const REEL_COUNT = 3
const ICON_HEIGHT = 120
const SET_HEIGHT = ICON_HEIGHT * SYMBOLS.length
const REPEATS = 12
// Idle scroll speed (px/sec) — also used to velocity-match the deceleration
// handoff, the same trick RouletteWheel.vue uses for its idle-to-landing spin.
const IDLE_LOOP_DURATION = 0.6
const IDLE_PX_PER_SEC = SET_HEIGHT / IDLE_LOOP_DURATION
const MIN_LOOPS = 2

// Repeating the symbol set gives each strip a seamless idle loop: animating
// from y=0 to y=-SET_HEIGHT looks identical to animating from y=-SET_HEIGHT
// to y=-2*SET_HEIGHT, since the same 7 symbols repeat, so the loop can reset
// instantly with no visible jump.
const stripSymbols = Array.from(
  { length: SYMBOLS.length * REPEATS },
  (_, i) => SYMBOLS[i % SYMBOLS.length],
)

const reelEls = ref([null, null, null])
const reelState = Array.from({ length: REEL_COUNT }, () => ({ y: 0, tween: null }))
const winners = ref([false, false, false])

const setReelEl = (el, i) => {
  reelEls.value[i] = el
}

const reelClass = (i) => computed(() => ({ winner: winners.value[i] }))
const reelClasses = [reelClass(0), reelClass(1), reelClass(2)]

const normalizeY = (y) => {
  const mod = y % SET_HEIGHT
  return mod > 0 ? mod - SET_HEIGHT : mod
}

const killTween = (i) => {
  const state = reelState[i]
  if (state.tween) {
    state.y = Number(gsap.getProperty(reelEls.value[i], 'y')) || 0
    state.tween.kill()
    state.tween = null
  }
}

/** Fast, seamless downward loop — started immediately so the machine feels alive while the request is in flight. */
const beginSpin = () => {
  winners.value = [false, false, false]

  for (let i = 0; i < REEL_COUNT; i++) {
    const el = reelEls.value[i]
    if (!el) continue

    killTween(i)
    gsap.set(el, { y: reelState[i].y })

    reelState[i].tween = gsap.to(el, {
      y: reelState[i].y - SET_HEIGHT,
      duration: IDLE_LOOP_DURATION,
      ease: 'none',
      repeat: -1,
    })
  }
}

const applyWinnerHighlight = (symbolIndexes) => {
  const allMatch = symbolIndexes[0] === symbolIndexes[1] && symbolIndexes[1] === symbolIndexes[2]
  if (allMatch) {
    winners.value = [true, true, true]
    return
  }

  const sevenFlags = symbolIndexes.map((symbol) => symbol === SEVEN_INDEX)
  winners.value = sevenFlags.filter(Boolean).length === 2 ? sevenFlags : [false, false, false]
}

/**
 * Decelerate each reel onto its target symbol, continuing in the same
 * downward direction, with reel i traveling further than reel i-1 so the
 * reels stop in a staggered cascade (matching the old page's 1s/1.5s/2s feel).
 */
const spinTo = (symbolIndexes) => {
  const landings = reelEls.value.map((el, i) => {
    return new Promise((resolve) => {
      if (!el) {
        resolve()
        return
      }

      killTween(i)

      const startY = reelState[i].y
      const targetIndex = symbolIndexes[i]
      const loopsNeeded = MIN_LOOPS + i

      const baseRow = Math.ceil(-startY / ICON_HEIGHT + loopsNeeded * SYMBOLS.length)
      const baseRowMod = ((baseRow % SYMBOLS.length) + SYMBOLS.length) % SYMBOLS.length
      const rowOffset = ((targetIndex - baseRowMod) + SYMBOLS.length) % SYMBOLS.length
      const finalRow = baseRow + rowOffset
      const finalY = -finalRow * ICON_HEIGHT

      const distance = startY - finalY
      // power1.out has initial velocity 2*distance/duration — matching that to
      // the idle speed means there's no visible speed jump at the handoff.
      const duration = (2 * distance) / IDLE_PX_PER_SEC

      reelState[i].y = finalY
      reelState[i].tween = gsap.to(el, {
        y: finalY,
        duration,
        ease: 'power1.out',
        onComplete: () => {
          reelState[i].tween = null
          resolve()
        },
      })
    })
  })

  return Promise.all(landings).then(() => {
    applyWinnerHighlight(symbolIndexes)

    // Fold each reel's (potentially large) landed offset back into one
    // symbol-set's worth of range, invisibly, so the next beginSpin() loop
    // keeps operating on small numbers.
    for (let i = 0; i < REEL_COUNT; i++) {
      const el = reelEls.value[i]
      if (!el) continue
      const normalized = normalizeY(reelState[i].y)
      reelState[i].y = normalized
      gsap.set(el, { y: normalized })
    }
  })
}

/** Stop mid-spin (API error) without a landing reveal. */
const cancelSpin = () => {
  for (let i = 0; i < REEL_COUNT; i++) {
    killTween(i)
    const el = reelEls.value[i]
    if (el) gsap.set(el, { y: reelState[i].y })
  }
}

onBeforeUnmount(() => {
  for (let i = 0; i < REEL_COUNT; i++) killTween(i)
})

defineExpose({ beginSpin, spinTo, cancelSpin })
</script>

<template>
  <div class="slot-machine">
    <div v-for="i in REEL_COUNT" :key="i - 1" class="reel-window" :class="reelClasses[i - 1].value">
      <div class="reel-strip" :ref="(el) => setReelEl(el, i - 1)">
        <div v-for="(symbol, idx) in stripSymbols" :key="idx" class="slot-icon">{{ symbol }}</div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.slot-machine {
  display: flex;
  gap: 15px;
  padding: 20px;
  background: #202225;
  border: 4px solid #ffffff;
  border-radius: 15px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
}

.reel-window {
  width: 100px;
  height: 120px;
  background: #111;
  border: 2px solid #000;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
  box-shadow: inset 0 0 15px rgba(0, 0, 0, 0.7);
  transform: translateZ(0);
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

.reel-window.winner {
  border-color: #3ba55d;
  box-shadow: 0 0 15px #3ba55d;
}

.reel-strip {
  display: flex;
  flex-direction: column;
  will-change: transform;
}

.slot-icon {
  width: 100px;
  height: 120px;
  flex-shrink: 0;
  background: #ffffff;
  font-size: 4rem;
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>
