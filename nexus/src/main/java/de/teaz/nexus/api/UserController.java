package de.teaz.nexus.api;

import de.teaz.nexus.website.service.EconomyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final EconomyService economyService;

    @Autowired
    public UserController(EconomyService economyService) {
        this.economyService = economyService;
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<Integer> getUserBalance(@PathVariable String userId) {
        return ResponseEntity.ok(economyService.getBalance(userId));
    }
}
