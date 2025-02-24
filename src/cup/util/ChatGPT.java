package cup.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONObject;

public class ChatGPT {
    private final String apiKey;
    
    public ChatGPT(String apiKey) {
        this.apiKey = apiKey;
    }
    
    public String getResponse(String prompt) {
    	String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \"" + normalize(prompt) + "\"}]}";
    	
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
    
    public String getResponse(String system, String prompt) {
    	String body = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"system\", \"content\": \"" + normalize(system) + "\"},{\"role\": \"user\", \"content\": \"" + normalize(prompt) + "\"}]}";
    	
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
    
    public String getImageResponse(String prompt, String imageURL) {
    	String body = "{\"model\":\"gpt-4o-mini\",\"messages\":[{\"role\":\"user\",\"content\":[{\"type\":\"text\",\"text\":\"" + prompt +"\"},{\"type\":\"image_url\",\"image_url\":{\"url\":\"" + imageURL + "\"}}]}],\"temperature\":1}";
    	
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
    
    public String normalize(String text) {
    	String output = text;
    	
    	output = output.replace("\"", "\\\"");
    	output = output.replace("\\", "\\\\");
    	output = output.replace("\"", "\\\"");

    	return output;
    }
} 