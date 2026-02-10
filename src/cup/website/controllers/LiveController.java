package cup.website.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import cup.website.service.EconomyService;

@Controller
public class LiveController {
	
	private final EconomyService economyService;

    public LiveController(EconomyService economyService) {
        this.economyService = economyService;
    }

    @GetMapping("/live")
    public String showLiveShop(@AuthenticationPrincipal OAuth2User principal, Model model) {
    	String discordId = principal.getAttribute("id");
    	String username = principal.getAttribute("username");
        String avatarHash = principal.getAttribute("avatar");
        
        String avatarUrl = (avatarHash != null) 
                ? String.format("https://cdn.discordapp.com/avatars/%s/%s.png", discordId, avatarHash)
                : "https://cdn.discordapp.com/embed/avatars/0.png";
        
        model.addAttribute("balance", economyService.getBalance(discordId));
        model.addAttribute("username", username);
        model.addAttribute("avatar", avatarUrl);
        
        return "live";
    }   
}
