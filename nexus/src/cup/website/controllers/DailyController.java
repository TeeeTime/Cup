package cup.website.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cup.website.service.EconomyService;

@Controller
public class DailyController {

	private final EconomyService economyService;
	
	public DailyController(EconomyService economyService) {
        this.economyService = economyService;
    }
	
	@PostMapping("/claim-daily")
    public String claimBonus(@AuthenticationPrincipal OAuth2User principal, RedirectAttributes redirectAttributes) {
		String discordId = principal.getAttribute("id");
        
        try {
        	economyService.redeemDaily(discordId);
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return "redirect:/home";
	}
}
