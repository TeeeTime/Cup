package de.teaz.nexus.api;

import de.teaz.nexus.dto.RouletteErrorResponse;
import de.teaz.nexus.dto.RouletteSpinRequest;
import de.teaz.nexus.dto.RouletteSpinResponse;
import de.teaz.nexus.website.service.RouletteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/roulette")
@CrossOrigin(origins = "http://localhost:5173")
public class RouletteController {

    private final RouletteService rouletteService;

    @Autowired
    public RouletteController(RouletteService rouletteService) {
        this.rouletteService = rouletteService;
    }

    @PostMapping("/spin")
    public ResponseEntity<?> spin(Authentication authentication, @RequestBody RouletteSpinRequest request) {
        String userId = authentication.getName();

        try {
            RouletteSpinResponse response = rouletteService.spin(
                    userId,
                    request.betType(),
                    request.betAmount()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new RouletteErrorResponse(e.getMessage()));
        }
    }
}
