package cup.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGPT {
    private final String apiKey;
    
    public ChatGPT(String apiKey) {
        this.apiKey = apiKey;
    }
    
    /*
     *	Request a text response with a system prompt
     */
    public String getTextResponse(String systemPrompt, String userPrompt) {
    	//	API request body
    	JSONObject requestBody = new JSONObject();
    	requestBody.put("model", "gpt-4o");
    	requestBody.put("temperature", 0.7); //Temperature of 0.7 has been great for responses with a tiny bit of spice in the past
    	
    	//	JSON array to store the messages
    	JSONArray messages = new JSONArray();
    	
    	//	Adding the system prompt
    	JSONObject systemMessage = new JSONObject();
    	systemMessage.put("role", "system");
    	systemMessage.put("content", systemPrompt);
    	messages.put(systemMessage);
    	
    	//	Adding the user prompt
    	JSONObject userMessage = new JSONObject();
    	userMessage.put("role", "user");
    	userMessage.put("content", userPrompt);
    	messages.put(userMessage);
    	
    	requestBody.put("messages", messages);
    	
    	//	Http request setting up URI, API key and the request body
    	HttpRequest request = HttpRequest.newBuilder()
    			.uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
    	
    	HttpClient client = HttpClient.newHttpClient();
    	
    	try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			JSONObject jsonResponse = new JSONObject(response.body());
			return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content").trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while trying to call openai API: " + e.getMessage();
		}
    }
    
    /*
     *	Request a text response with image context with a system prompt
     */
    public String getTextResponse(String systemPrompt, String userPrompt, String image) {
    	//	API request body
    	JSONObject requestBody = new JSONObject();
    	requestBody.put("model", "gpt-4o");
    	requestBody.put("temperature", 0.7); //Temperature of 0.7 has been great for responses with a tiny bit of spice in the past
    	
    	//	JSON array to store the messages
    	JSONArray messages = new JSONArray();
    	
    	//	Adding the system prompt
    	JSONObject systemMessage = new JSONObject();
    	systemMessage.put("role", "system");
    	systemMessage.put("content", systemPrompt);
    	messages.put(systemMessage);
    	
    	//	Adding the user prompt
    	JSONObject userMessage = new JSONObject();
    	userMessage.put("role", "user");
    	JSONArray userContent = new JSONArray();
    	
    	JSONObject textContent = new JSONObject();
    	textContent.put("type", "text");
    	textContent.put("text", userPrompt);
    	userContent.put(textContent);
    	
    	JSONObject imageContent = new JSONObject();
    	imageContent.put("type", "image_url");
    	JSONObject imageUrl = new JSONObject();
    	imageUrl.put("url", image);
    	imageContent.put("image_url", imageUrl);
    	userContent.put(imageContent);
    	
    	userMessage.put("content", userContent);
    	messages.put(userMessage);
    	
		requestBody.put("messages", messages);
    	
    	//	Http request setting up URI, API key and the request body
    	HttpRequest request = HttpRequest.newBuilder()
    			.uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
    	
    	HttpClient client = HttpClient.newHttpClient();
    	
    	try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			JSONObject jsonResponse = new JSONObject(response.body());
			return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content").trim();
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while trying to call openai API: " + e.getMessage();
		}
    }
    
    /*
     *	Request a image response
     */
    public String getImageResponse(String userPrompt) {
    	//	API request body
    	JSONObject requestBody = new JSONObject();
    	requestBody.put("model", "dall-e-2");
    	requestBody.put("prompt", userPrompt);
    	requestBody.put("n", 1);
    	requestBody.put("size", "1024x1024");
    	
    	//	Http request setting up URI, API key and the request body
    	HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/images/generations"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
    	
    	HttpClient client = HttpClient.newHttpClient();
    	try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			JSONObject jsonResponse = new JSONObject(response.body());
			if (jsonResponse.has("error")) {
				return "Error: " + jsonResponse.getJSONObject("error").getString("message");
			}
			return jsonResponse.getJSONArray("data").getJSONObject(0).getString("url");
		} catch (IOException | InterruptedException e) {
			return "Error while trying to generate image: " + e.getMessage();
		}
    }
    
    /*
     *	Call the API with tools. If a tool response is returned, call the appropriate tool. Else, get a normal text response 
     */
    public String getAssistantResponse(String userPrompt, String image) {
    	//	API request body
    	JSONObject requestBody = new JSONObject();
    	requestBody.put("model", "gpt-4o");
    	
    	//	Defining tools
    	JSONArray tools = new JSONArray();
    	
    	//	Defining generate_image tool
    	JSONObject generateImageTool = new JSONObject();
    	generateImageTool.put("type", "function");
    	
    	JSONObject generateImageFunction = new JSONObject();
    	generateImageFunction.put("name", "generate_image");
    	generateImageFunction.put("description", "Generates an image based on a text description. Use this ONLY when the user explicitly asks to draw, create, or generate a picture.");
    	
    	JSONObject parameters = new JSONObject();
    	parameters.put("type", "object");
    	
    	JSONObject promptProp = new JSONObject();
    	promptProp.put("type", "string");
    	promptProp.put("description", "The detailed visual description of the image to generate.");
    	
    	JSONObject properties = new JSONObject();
    	properties.put("prompt", promptProp);
    	
    	parameters.put("properties", properties);
    	parameters.put("required", new JSONArray().put("prompt"));
    	
    	generateImageFunction.put("parameters", parameters);
    	generateImageTool.put("function", generateImageFunction);
    	tools.put(generateImageTool);
    	
    	//	JSON array to store the messages
    	JSONArray messages = new JSONArray();
    	
    	//	Adding the system prompt
    	JSONObject systemMessage = new JSONObject();
    	systemMessage.put("role", "system");
    	systemMessage.put("content", "You are an assistant ai in a discord server. Your name is \"TEAZ\". You reply in a casual and funny way. You should provide valuable information. Always reply in english. You may use emojis with discord formating.");
    	messages.put(systemMessage);
    	
    	//	Adding user prompt
    	JSONObject userMessage = new JSONObject();
    	userMessage.put("role", "user");
    	userMessage.put("content", userPrompt);
    	messages.put(userMessage);
    	
    	requestBody.put("messages", messages);
    	requestBody.put("tools", tools);
    	requestBody.put("tool_choice", "auto");
    	
    	//	Http request setting up URI, API key and the request body
    	HttpRequest request = HttpRequest.newBuilder()
    			.uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();
    	
    	HttpClient client = HttpClient.newHttpClient();
    	
    	try {
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			
			JSONObject jsonResponse = new JSONObject(response.body());
			JSONObject message = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
			
			//	Check for tool calls 
			if(message.has("tool_calls")) {
				JSONArray toolCalls = message.getJSONArray("tool_calls");
				for(int i = 0; i < toolCalls.length(); i++) {
					JSONObject toolCall = toolCalls.getJSONObject(i);
					String toolName = toolCall.getJSONObject("function").getString("name");
					
					if(toolName.equals("generate_image")) {
						String argsString = toolCall.getJSONObject("function").getString("arguments");
	                    JSONObject args = new JSONObject(argsString);
	                    String imagePrompt = args.getString("prompt");
	                    
	                    
	                    return getImageResponse(imagePrompt);
					}
				}
				
			//	Check if a image URL as context has been added
			}else if(!image.equals("NONE")) {
				return getTextResponse("You are an assistant ai in a discord server. Your name is \"TEAZ\". You reply in a casual and funny way. You should provide valuable information. If there is text on the image thats not in german or english, translate it! When asked for a location, provide a guess with the data provided! Always reply in english. You may use emojis with discord formating."
						, userPrompt, image);
				
			//	Default case with no context
			}else {
				return getTextResponse("You are an assistant ai in a discord server. Your name is \"TEAZ\". You reply in a casual and funny way. You should provide valuable information. If there is text on the image thats not in german or english, translate it! When asked for a location, provide a guess with the data provided! Always reply in english. You may use emojis with discord formating."
						, userPrompt);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error while trying to call openai API: " + e.getMessage();
		}
    	return "Error while trying to process the request";
    }
}

//IF YOU READ THIS, YOU READ THROUGH THE MOST BORING CLASS IN THE PROJECT