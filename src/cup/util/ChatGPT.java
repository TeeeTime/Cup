package cup.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class ChatGPT {
    private final String apiKey;
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    
    public ChatGPT(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getResponse(String prompt) {
    	String body = "{\"model\": \"gpt-4o\", \"messages\": [{\"role\": \"user\", \"content\": \"" + prompt + "\"}]}";
    	
    	HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
    	
    	HttpClient client = HttpClient.newHttpClient();
    	try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			JSONObject jsonResponse = new JSONObject(response.body());
			return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content").trim();
		} catch (IOException | InterruptedException e) {
			return "Error while trying to call openai: " + e.getMessage();
		}
    }
} 