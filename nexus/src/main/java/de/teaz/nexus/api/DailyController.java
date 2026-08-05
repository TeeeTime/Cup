package de.teaz.nexus.api;

import de.teaz.nexus.discord.commands.DailyCommand;
import de.teaz.nexus.dto.DailyRewardStatus;
import de.teaz.nexus.website.service.EconomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/daily")
@CrossOrigin(origins = "http://localhost:5173")
public class DailyController {

    private final EconomyService economyService;

    @Autowired
    public DailyController(EconomyService economyService) {
        this.economyService = economyService;
    }

    @GetMapping("/{userId}/status")
    public ResponseEntity<DailyRewardStatus> getDailyRewardStatus(@PathVariable String userId) {
        return ResponseEntity.ok(economyService.getDailyStatus(userId));
    }

    @PostMapping("/claim")
    public ResponseEntity<DailyRewardStatus> claimDailyReward(Authentication authentication) {
        String userId = authentication.getName();

        if (!economyService.isDailyRedeemable(userId)) {
            return ResponseEntity.badRequest().build();
        }

        economyService.redeemDaily(userId);

        return ResponseEntity.ok(economyService.getDailyStatus(userId));
    }
}
