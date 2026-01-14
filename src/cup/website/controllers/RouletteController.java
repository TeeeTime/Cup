package cup.website.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RouletteController {
	
	@PostMapping("/games/roulette")
	public String slots(@AuthenticationPrincipal OAuth2User principal, Model model) {
		return "slots";
	}
	
	

}
