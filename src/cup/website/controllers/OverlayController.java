package cup.website.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OverlayController {

	@GetMapping("/overlay")
	public String showOverlay() {
	    return "overlay";
	}
}
