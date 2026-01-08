package cup.website.controllers;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import cup.website.model.LeaderboardEntry;
import cup.website.service.EconomyService;

@Controller
public class WebController {
	
	private final EconomyService economyService;

    public WebController(EconomyService economyService) {
        this.economyService = economyService;
    }
    
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/home")
    public String home(@AuthenticationPrincipal OAuth2User principal, Model model) {
		if (principal == null) return "redirect:/"; // Safety fallback

        String discordId = principal.getAttribute("id");
        String username = principal.getAttribute("username");
        String avatarHash = principal.getAttribute("avatar");

        String avatarUrl = (avatarHash != null) 
            ? String.format("https://cdn.discordapp.com/avatars/%s/%s.png", discordId, avatarHash)
            : "https://cdn.discordapp.com/embed/avatars/0.png"; 

        int balance = economyService.getBalance(discordId);
        
        List<LeaderboardEntry> leaderboard = economyService.getLeaderboard();

        model.addAttribute("username", username);
        model.addAttribute("avatar", avatarUrl);
        model.addAttribute("balance", balance);
        model.addAttribute("leaderboard", leaderboard);

        return "home";
	}

}
