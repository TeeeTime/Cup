package cup.website.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cup.economy.LeaderboardEntry;
import cup.twitch.LiveListener;
import cup.website.service.EconomyService;

@Controller
public class WebController {
	
	private final EconomyService economyService;

    public WebController(EconomyService economyService) {
        this.economyService = economyService;
    }
    
	@GetMapping("/")
	public String index() {
		return "index";
	}
	
	@GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
		if (principal == null) return "redirect:/";

        String discordId = principal.getAttribute("id");
        String username = principal.getAttribute("username");
        String avatarHash = principal.getAttribute("avatar");

        String avatarUrl = (avatarHash != null) 
            ? String.format("https://cdn.discordapp.com/avatars/%s/%s.png", discordId, avatarHash)
            : "https://cdn.discordapp.com/embed/avatars/0.png"; 

        int balance = economyService.getBalance(discordId);
        
        List<LeaderboardEntry> leaderboard = economyService.getLeaderboard();
        
        int streak = economyService.getStreak(discordId);

        boolean redeemable = economyService.isDailyRedeemable(discordId);
        
        String timeLeft = "";
        
        if(!redeemable) {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = LocalDate.now().atTime(LocalTime.MAX);
            
            long secondsUntilMidnight = java.time.Duration.between(now, midnight).getSeconds();
            long hours = secondsUntilMidnight / 3600;
            long minutes = (secondsUntilMidnight % 3600) / 60;
            
            timeLeft = hours + "h " + minutes + "m";
        }
        
        int nextStreakGoal = streak - (streak % 7) + 7;
        
        int streakGoalProgress = 0;
        
        if(streak > 0) {
        	streakGoalProgress = (streak - 1) % 7 + 1;
        }
        
        boolean twitchLive = LiveListener.isLive;
        
        model.addAttribute("username", username);
        model.addAttribute("avatar", avatarUrl);
        model.addAttribute("balance", balance);
        model.addAttribute("leaderboard", leaderboard);
        model.addAttribute("currentStreak", streak);
        model.addAttribute("dailyReady", redeemable);
        model.addAttribute("dailyTimeLeft", timeLeft);
        model.addAttribute("nextStreakGoal", nextStreakGoal);
        model.addAttribute("streakGoalProgress", streakGoalProgress);
        model.addAttribute("twitchLive", twitchLive);
        

        return "home";
	}

}
