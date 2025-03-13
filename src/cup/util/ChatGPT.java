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
    	String body = "{\"model\": \"gpt-4o\", \"messages\": [{\"role\": \"user\", \"content\": \"" + escape(prompt) + "\"}]}";
    	
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
    	String body = "{\"model\": \"gpt-4o\", \"messages\": [{\"role\": \"system\", \"content\": \"" + escape(system) + "\"},{\"role\": \"user\", \"content\": \"" + escape(prompt) + "\"}]}";
    	
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
    	String body = "{\"model\":\"gpt-4o-mini\",\"messages\":[{\"role\":\"user\",\"content\":[{\"type\":\"text\",\"text\":\"" + escape(prompt) +"\"},{\"type\":\"image_url\",\"image_url\":{\"url\":\"" + imageURL + "\"}}]}],\"temperature\":0.7}";
    	
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
    
    public String escape(String text) {
    	String output = text;
    	
    	output = output.replace("\\", "\\\\");
    	output = output.replace("\"", "\\\"");
    	output = output.replace("\b", "\\b");
    	output = output.replace("\f", "\\f");
        output = output.replace("\n", "\\n");
        output = output.replace("\r", "\\r");
        output = output.replace("\t", "\\t");
    	
    	return output;
    }
} 