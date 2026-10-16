package de.teaz.nexus.api;

import de.teaz.nexus.dto.SlotsErrorResponse;
import de.teaz.nexus.dto.SlotsSpinRequest;
import de.teaz.nexus.dto.SlotsSpinResponse;
import de.teaz.nexus.website.service.SlotsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/slots")
@CrossOrigin(origins = "http://localhost:5173")
public class SlotsController {

    private final SlotsService slotsService;

    @Autowired
    public SlotsController(SlotsService slotsService) {
        this.slotsService = slotsService;
    }

    @PostMapping("/spin")
    public ResponseEntity<?> spin(Authentication authentication, @RequestBody SlotsSpinRequest request) {
        String userId = authentication.getName();

        try {
            SlotsSpinResponse response = slotsService.spin(
                    userId,
                    request.betAmount()
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new SlotsErrorResponse(e.getMessage()));
        }
    }
}
