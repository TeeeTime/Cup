package de.teaz.nexus.website.controllers;

import de.teaz.nexus.website.service.EconomyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class SlotsController {

    private final EconomyService economyService;
    private final Random random = new Random();

    private static final String[] SYMBOLS = {"🍒", "🍋", "🍊", "🫐", "🍇", "💎", "7️⃣"};

    private static final Map<String, Integer> PAYOUTS = Map.of(
            "🍒", 15,
            "🍋", 15,
            "🍊", 25,
            "🫐", 25,
            "🍇", 50,
            "💎", 75,
            "7️⃣", 100
    );

    public SlotsController(EconomyService economyService) {
        this.economyService = economyService;
    }

    @GetMapping("/games/slots")
    public String showSlotsPage(@AuthenticationPrincipal OAuth2User user, Model model) {
        String userId = user.getAttribute("id");
        model.addAttribute("balance", economyService.getBalance(userId));
        return "slots";
    }

    @PostMapping("/api/slots/spin")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> spinSlots(@AuthenticationPrincipal OAuth2User user, @RequestParam int betAmount) {

        String userId = user.getAttribute("id");
        Map<String, Object> response = new HashMap<>();

        if (betAmount <= 0 || economyService.getBalance(userId) < betAmount) {
            response.put("error", "Insufficient funds.");
            return ResponseEntity.badRequest().body(response);
        }

        economyService.setBalance(userId, economyService.getBalance(userId) - betAmount);

        int index1 = random.nextInt(SYMBOLS.length);
        int index2 = random.nextInt(SYMBOLS.length);
        int index3 = random.nextInt(SYMBOLS.length);

        String s1 = SYMBOLS[index1];
        String s2 = SYMBOLS[index2];
        String s3 = SYMBOLS[index3];

        boolean won = false;
        int payout = 0;

        if (s1.equals(s2) && s2.equals(s3)) {
            won = true;
            payout = betAmount * PAYOUTS.get(s1);
        } 

        else if ((s1.equals("7️⃣") && s2.equals("7️⃣")) || 
                 (s2.equals("7️⃣") && s3.equals("7️⃣")) || 
                 (s1.equals("7️⃣") && s3.equals("7️⃣"))) {
             won = true;
             payout = betAmount * 2;
        }

        if (won) {
            economyService.setBalance(userId, economyService.getBalance(userId) + payout);
        }

        response.put("symbolIndexes", new int[]{index1, index2, index3});
        response.put("symbols", new String[]{s1, s2, s3});
        response.put("won", won);
        response.put("payout", payout);
        response.put("newBalance", economyService.getBalance(userId));

        return ResponseEntity.ok(response);
    }
}
