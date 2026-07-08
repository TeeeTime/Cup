package de.teaz.nexus.auth.controller;

import de.teaz.nexus.auth.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.security.oauth2.client.registration.discord.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.discord.client-secret}")
    private String clientSecret;

    private final String redirectUri = "http://localhost:5173/login";

    @Autowired
    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @PostMapping("/discord")
    public ResponseEntity<?> loginWithDiscord(@RequestBody Map<String, String> payload) {
        String code = payload.get("code");

        if (code == null) {
            return ResponseEntity.badRequest().body("No authorization code provided");
        }

        try {
            String discordAccessToken = getDiscordAccessToken(code);

            Map<String, String> profile = getDiscordUserProfile(discordAccessToken);
            String discordId = profile.get("id");

            String jwt = jwtService.generateToken(discordId);

            // 4. Return the token to Vue
            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "userId", discordId,
                    "username", profile.get("username"),
                    "avatarUrl", profile.get("avatarUrl")
            ));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Discord authentication failed");
        }
    }


    private String getDiscordAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);
        map.add("grant_type", "authorization_code");
        map.add("code", code);
        map.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(
                "https://discord.com/api/oauth2/token",
                request,
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    private Map<String, String> getDiscordUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                "https://discord.com/api/users/@me",
                HttpMethod.GET,
                request,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        String id = (String) body.get("id");
        String username = (String) body.get("username");
        String avatarHash = (String) body.get("avatar");

        // Construct the Discord Avatar Image URL
        String avatarUrl;
        if (avatarHash != null) {
            avatarUrl = "https://cdn.discordapp.com/avatars/" + id + "/" + avatarHash + ".png";
        } else {
            // Fallback to the default gray Discord logo if they have no profile picture
            avatarUrl = "https://cdn.discordapp.com/embed/avatars/0.png";
        }

        return Map.of(
                "id", id,
                "username", username,
                "avatarUrl", avatarUrl
        );
    }
}
