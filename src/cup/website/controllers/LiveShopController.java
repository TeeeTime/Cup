package cup.website.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cup.website.model.Redemption;
import cup.website.service.EconomyService;
import cup.website.service.RedemptionQueueService;
import cup.website.service.YoutubeService;

@RestController
@RequestMapping("/api/shop")
public class LiveShopController {
	
	@Autowired
    private YoutubeService youtubeService;
	
	@Autowired
	private RedemptionQueueService queueService;
	
	@Autowired
	private EconomyService economyService;

    private static final int COINS_PER_SECOND = 20;

    @GetMapping("/check-video")
    public ResponseEntity<?> checkVideo(@RequestParam String url) {
        String videoId = youtubeService.extractVideoId(url);
        
        if (videoId == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid YouTube URL"));
        }

        try {
            // Use the modular service to get duration
            long seconds = youtubeService.getVideoDuration(videoId);
            long cost = seconds * COINS_PER_SECOND;

            return ResponseEntity.ok(Map.of(
                "videoId", videoId,
                "durationSeconds", seconds,
                "cost", cost,
                "formattedTime", String.format("%dm %ds", seconds / 60, seconds % 60)
            ));

        } catch (Exception e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }
    

    @PostMapping("/buy/media")
    public ResponseEntity<?> buyMedia(@AuthenticationPrincipal OAuth2User principal, @RequestParam String url, @RequestParam long durationSeconds) {

        String discordId = principal.getAttribute("id");
        String username = principal.getAttribute("username");

        // 2. Validate Inputs
        if (durationSeconds <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid video duration"));
        }

        // 3. Calculate Cost
        int cost = (int) (durationSeconds * COINS_PER_SECOND);

        // 4. Check Balance
        if (economyService.getBalance(discordId) < cost) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient funds"));
        }

        // 5. Deduct Coins (Transaction)
        // We get current balance, subtract cost, and update it
        int currentBalance = economyService.getBalance(discordId);
        economyService.setBalance(discordId, currentBalance - cost);

        // 6. Create the Request Object (POJO)
        Redemption redemption = new Redemption();
        redemption.setUserId(discordId);
        redemption.setUsername(username);
        redemption.setType("MEDIA");
        redemption.setContent(url);
        redemption.setDuration(durationSeconds);
        redemption.setCost(cost);

        // 7. Add to In-Memory Queue
        queueService.add(redemption);

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Video added to queue!",
            "newBalance", economyService.getBalance(discordId)
        ));
    }
    
    @PostMapping("/buy/tts")
    public ResponseEntity<?> buyTTS(
            @AuthenticationPrincipal OAuth2User principal,
            @RequestParam String message) {

        String discordId = principal.getAttribute("id");
        String username = principal.getAttribute("username");
        int cost = 100; // Fixed cost for TTS

        // 1. Validation
        if (message == null || message.isBlank() || message.length() > 300) {
            return ResponseEntity.badRequest().body(Map.of("error", "Invalid message length"));
        }

        // 2. Check Balance
        if (economyService.getBalance(discordId) < cost) {
            return ResponseEntity.badRequest().body(Map.of("error", "Insufficient funds"));
        }

        // 3. Deduct Coins
        int currentBalance = economyService.getBalance(discordId);
        economyService.setBalance(discordId, currentBalance - cost);

        // 4. Add to Queue
        Redemption r = new Redemption();
        r.setUserId(discordId);
        r.setUsername(username);
        r.setType("TTS");     // <--- Important Type
        r.setContent(message); // <--- The text to read
        r.setCost(cost);
        r.setDuration(5);      // Estimated duration (overlay handles actual end)

        queueService.add(r);

        return ResponseEntity.ok(Map.of(
            "success", true, 
            "message", "Message sent!",
            "newBalance", economyService.getBalance(discordId)
        ));
    }

}
