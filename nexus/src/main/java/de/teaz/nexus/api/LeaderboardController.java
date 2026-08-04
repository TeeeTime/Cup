package de.teaz.nexus.api;

import de.teaz.nexus.dto.LeaderboardEntry;
import de.teaz.nexus.website.service.EconomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/leaderboard")
public class LeaderboardController {

    private final EconomyService economyService;

    @Autowired
    public LeaderboardController(EconomyService economyService) {
        this.economyService = economyService;
    }

    @GetMapping
    public ResponseEntity<List<LeaderboardEntry>> getLeaderboard() {
        List<LeaderboardEntry> leaderboard = economyService.getLeaderboard();
        return ResponseEntity.ok(leaderboard);
    }
}
