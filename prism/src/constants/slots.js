// Index-aligned with nexus's SlotsService.PAYOUTS — the backend only ever
// returns symbol indexes, never emoji, so this order must match exactly.
export const SYMBOLS = ['🍒', '🍋', '🍊', '🫐', '🍇', '💎', '7️⃣']

// Presentational only — real payouts always come from the server response.
export const PAYOUTS = [15, 15, 25, 25, 50, 75, 100]

export const SEVEN_INDEX = 6
export const TWO_SEVENS_PAYOUT = 2

export const RESULT_COLORS = {
  LOSE: '#ed4245',
  WIN: '#3ba55d',
}
