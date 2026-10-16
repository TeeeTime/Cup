package de.teaz.nexus.website.service;

import de.teaz.nexus.dto.SlotsSpinResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SlotsService {

    private static final int SYMBOL_COUNT = 7;
    private static final int SEVEN_INDEX = 6;

    // Index-aligned with prism/src/constants/slots.js SYMBOLS: cherry, lemon, orange, blueberry, grape, diamond, seven
    private static final int[] PAYOUTS = { 15, 15, 25, 25, 50, 75, 100 };

    private final EconomyService economyService;
    private final Random random = new Random();

    @Autowired
    public SlotsService(EconomyService economyService) {
        this.economyService = economyService;
    }

    public SlotsSpinResponse spin(String userId, int betAmount) {
        if (betAmount <= 0 || economyService.getBalance(userId) < betAmount) {
            throw new IllegalArgumentException("Invalid bet or insufficient funds.");
        }

        economyService.setBalance(userId, economyService.getBalance(userId) - betAmount);

        int[] symbolIndexes = new int[] {
                random.nextInt(SYMBOL_COUNT),
                random.nextInt(SYMBOL_COUNT),
                random.nextInt(SYMBOL_COUNT)
        };

        boolean won = false;
        int payout = 0;

        if (symbolIndexes[0] == symbolIndexes[1] && symbolIndexes[1] == symbolIndexes[2]) {
            won = true;
            payout = betAmount * PAYOUTS[symbolIndexes[0]];
        } else if (countSevens(symbolIndexes) == 2) {
            won = true;
            payout = betAmount * 2;
        }

        economyService.setBalance(userId, economyService.getBalance(userId) + payout);

        return new SlotsSpinResponse(
                symbolIndexes,
                won,
                payout,
                economyService.getBalance(userId)
        );
    }

    private static int countSevens(int[] symbolIndexes) {
        int count = 0;
        for (int symbolIndex : symbolIndexes) {
            if (symbolIndex == SEVEN_INDEX) {
                count++;
            }
        }
        return count;
    }
}
