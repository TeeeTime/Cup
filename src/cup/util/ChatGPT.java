package cup.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

public class ChatGPT {
    private final String apiKey;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    
    public ChatGPT(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getResponse(String prompt) {
        try {
            // Create connection
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);
            
            // Create request body
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "gpt-3.5-turbo");
            requestBody.put("messages", new JSONObject[]{ 
                new JSONObject()
                    .put("role", "user")
                    .put("content", prompt)
            });
            requestBody.put("temperature", 0.7);
            
            // Send request
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = requestBody.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            
            // Read response
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
            }
            
            // Parse response
            JSONObject jsonResponse = new JSONObject(response.toString());
            return jsonResponse.getJSONArray("choices")
                             .getJSONObject(0)
                             .getJSONObject("message")
                             .getString("content")
                             .trim();
            
        } catch (Exception e) {
            return "Error getting ChatGPT response: " + e.getMessage();
        }
    }
} 