package cup.website.controllers;

import cup.website.service.EconomyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class RouletteController {

    private final EconomyService economyService;
    private final Random random = new Random();

    private static final List<Integer> RED_NUMBERS = Arrays.asList(
            1, 3, 5, 7, 9, 12, 14, 16, 18, 19, 21, 23, 25, 27, 30, 32, 34, 36
    );

    public RouletteController(EconomyService economyService) {
        this.economyService = economyService;
    }

    @GetMapping("/games/roulette")
    public String showRoulettePage(@AuthenticationPrincipal OAuth2User user, Model model) {
        String userId = user.getAttribute("id");
        model.addAttribute("balance", economyService.getBalance(userId));
        return "roulette"; // Loads roulette.html
    }

    @PostMapping("/api/roulette/spin")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> spinApi(
            @AuthenticationPrincipal OAuth2User user,
            @RequestParam String betType,
            @RequestParam(defaultValue = "0") int betNumber,
            @RequestParam int betAmount) {

        String userId = user.getAttribute("id");
        Map<String, Object> response = new HashMap<>();

        if (betAmount <= 0 || economyService.getBalance(userId) < betAmount) {
            response.put("error", "Invalid bet or insufficient funds.");
            return ResponseEntity.badRequest().body(response);
        }

        economyService.setBalance(userId, economyService.getBalance(userId) - betAmount);

        int resultNumber = random.nextInt(37);
        
        String resultColor = "GREEN";
        if (RED_NUMBERS.contains(resultNumber)) {
            resultColor = "RED";
        } else if (resultNumber != 0) {
            resultColor = "BLACK";
        }

        boolean won = false;
        int payout = 0;

        if (betType.equals("RED") && "RED".equals(resultColor)) {
            won = true;
            payout = betAmount * 2;
        } else if (betType.equals("BLACK") && "BLACK".equals(resultColor)) {
            won = true;
            payout = betAmount * 2;
        } else if (betType.equals("GREEN")) {
            if ("GREEN".equals(resultColor)) {
                won = true;
                payout = betAmount * 36; 
            }
        }

        economyService.setBalance(userId, economyService.getBalance(userId) + payout);
        
        response.put("resultNumber", resultNumber);
        response.put("resultColor", resultColor);
        response.put("won", won);
        response.put("payout", payout);
        response.put("newBalance", economyService.getBalance(userId));

        return ResponseEntity.ok(response);
    }
}
