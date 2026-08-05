package de.teaz.nexus.website.service;

import de.teaz.nexus.dto.RouletteSpinResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class RouletteService {

    private static final List<Integer> RED_NUMBERS = Arrays.asList(
            1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36
    );

    private final EconomyService economyService;
    private final Random random = new Random();

    @Autowired
    public RouletteService(EconomyService economyService) {
        this.economyService = economyService;
    }

    public RouletteSpinResponse spin(String userId, String betType, int betAmount) {
        if (betAmount <= 0 || economyService.getBalance(userId) < betAmount) {
            throw new IllegalArgumentException("Invalid bet or insufficient funds.");
        }

        if (!isValidBetType(betType)) {
            throw new IllegalArgumentException("Invalid bet or insufficient funds.");
        }

        economyService.setBalance(userId, economyService.getBalance(userId) - betAmount);

        int resultNumber = random.nextInt(37);
        String resultColor = resolveColor(resultNumber);

        boolean won = false;
        int payout = 0;

        if ("RED".equals(betType) && "RED".equals(resultColor)) {
            won = true;
            payout = betAmount * 2;
        } else if ("BLACK".equals(betType) && "BLACK".equals(resultColor)) {
            won = true;
            payout = betAmount * 2;
        } else if ("GREEN".equals(betType) && "GREEN".equals(resultColor)) {
            won = true;
            payout = betAmount * 36;
        }

        economyService.setBalance(userId, economyService.getBalance(userId) + payout);

        return new RouletteSpinResponse(
                resultNumber,
                resultColor,
                won,
                payout,
                economyService.getBalance(userId)
        );
    }

    private static boolean isValidBetType(String betType) {
        return "RED".equals(betType) || "BLACK".equals(betType) || "GREEN".equals(betType);
    }

    private static String resolveColor(int resultNumber) {
        if (resultNumber == 0) {
            return "GREEN";
        }
        if (RED_NUMBERS.contains(resultNumber)) {
            return "RED";
        }
        return "BLACK";
    }
}
