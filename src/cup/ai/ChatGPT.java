package cup.ai;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import cup.util.Mapbox;

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
    public Response getAssistantResponse(String userPrompt, String image) {
    	//	API request body
    	JSONObject requestBody = new JSONObject();
    	requestBody.put("model", "gpt-4o");
    	
    	//	Defining tools
    	JSONArray tools = new JSONArray();
    	tools.put(getGenerateImageTool());
    	tools.put(getGenerateLocationMapTool());
    	
    	//	JSON array to store the messages
    	JSONArray messages = new JSONArray();
    	
    	//	Adding the system prompt
    	String systemPromptContent = """
    			You are TEAZ, a casual, funny, and helpful AI assistant in a Discord server. Always reply in English. You use emojis and Discord formatting (markdown) to make your messages look great.

    			### YOUR CAPABILITIES & TOOLS:
    			1.  **Image Generation:** If the user asks for a picture, drawing, or art, you MUST call the `generate_image` tool.
    			2.  **Geolocation (CRITICAL):** If the user provides an image and asks "Where is this?", "Find this location", or implies a location search, you MUST call the `generate_location_map` tool. Do NOT describe coordinates in text.
    			3.  **General Chat:** For everything else, chat casually. If an image contains text in a foreign language (non-English/German), translate it automatically.

    			### RULES FOR GEOLOCATION:
    			* **Trigger:** Any visual cue implying a location search.
    			* **Confidence:** If you are unsure of the exact spot, guess the city or region and lower the `zoom` level in the tool call.
    			* **Output:** Never output raw latitude/longitude in your text response. Always use the map tool.

    			### PERSONALITY:
    			* Be witty but helpful.
    			* Keep responses concise unless asked for detail.
    			""";
    	
    	JSONObject systemMessage = new JSONObject();
    	systemMessage.put("role", "system");
    	systemMessage.put("content", systemPromptContent);
    	messages.put(systemMessage);
    	
    	//	Adding user prompt
    	JSONObject userMessage = new JSONObject();
    	userMessage.put("role", "user");
    	
    	if(!image.equals("NONE")) {
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
    	}else {
        	userMessage.put("content", userPrompt);
    	}

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
	                    
	                    Response responseMessage = new Response(getTextResponse("You are an ai that just generated an image. You get the image prompt and write a short message (max 15 words) stating that and what you generated in a fun way. You may use emojis with discord formating.", imagePrompt));
	                    responseMessage.addImageURL(getImageResponse(imagePrompt));
	                    
	                    return responseMessage;
					}else if(toolName.equals("generate_location_map")) {
						String argsString = toolCall.getJSONObject("function").getString("arguments");
	                    JSONObject args = new JSONObject(argsString);
						String summary = args.getString("summary");
						double latitude = args.getDouble("latitude");
						double longitude = args.getDouble("longitude");
						int zoom = args.getInt("zoom");
						
						Mapbox mapbox = new Mapbox();
						String mapURL = mapbox.getLocationMapURL("pk.eyJ1IjoidGVlZXRpbWUiLCJhIjoiY21pcGIxbzE1MDVncTNncGo5azJhdXVwaSJ9.Z4YLtq-cH9kq4mWU3E9o_A", "satellite-streets-v12", latitude, longitude, zoom);
						
						Response responseMessage = new Response(summary + " **(confidence: " + (int) (((double) zoom / 18) * 100) + "%)**");
						responseMessage.addImageURL(mapURL);
						
						return responseMessage;
					}
				}
			}	
			
			if (message.has("content") && !message.isNull("content")) { 
				String chatResponse = message.getString("content");
				    
				return new Response(chatResponse);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new Response("Error while trying to call openai API: " + e.getMessage());
		}
    	return new Response("Error while trying to process the request");
    }
    
    private JSONObject getGenerateImageTool() {
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
    	
    	return generateImageTool;
    }
    
    private JSONObject getGenerateLocationMapTool() {
    	//	Define generate_location_map tool
    	JSONObject generateLocationMapTool = new JSONObject();
    	generateLocationMapTool.put("type", "function");
    	
    	JSONObject generateLocationMapFunction = new JSONObject();
    	generateLocationMapFunction.put("name", "generate_location_map");
    	generateLocationMapFunction.put("description", "Triggers a map view for any location inquiry. Use this whenever the user provides an image and asks for its location, coordinates, or country. It replaces text descriptions of coordinates.");
    	
    	JSONObject parameters = new JSONObject();
    	parameters.put("type", "object");
    	
    	JSONObject properties = new JSONObject();
    	
    	JSONObject latitudeProp = new JSONObject();
    	latitudeProp.put("type", "number");
    	latitudeProp.put("description", "The estimated latitude of the location");
    	properties.put("latitude", latitudeProp);
    	
    	JSONObject longitudeProp = new JSONObject();
    	longitudeProp.put("type", "number");
    	longitudeProp.put("description", "The estimated longitude of the location");
    	properties.put("longitude", longitudeProp);
    	
    	JSONObject zoomProp = new JSONObject();
    	zoomProp.put("type", "integer");
    	zoomProp.put("description", "The zoom level (1-18). Use 15-18 for precise matches, 10-12 for city, 4-6 for country.");
    	properties.put("zoom", zoomProp);
    	
    	JSONObject summaryProp = new JSONObject();
    	summaryProp.put("type", "string");
    	summaryProp.put("description", "A 5-word summary of the location (e.g., 'Near Eiffel Tower, Paris').");
    	properties.put("summary", summaryProp);
    	
    	parameters.put("properties", properties);
    	parameters.put("additionalProperties", false);
    	
    	JSONArray required = new JSONArray();
    	required.put("latitude");
        required.put("longitude");
        required.put("zoom");
        required.put("summary");
        parameters.put("required", required);
        
        generateLocationMapFunction.put("parameters", parameters);
        generateLocationMapFunction.put("strict", true);
        
        generateLocationMapTool.put("function", generateLocationMapFunction);

    	return generateLocationMapTool;
    }
}

//IF YOU READ THIS, YOU READ THROUGH THE MOST BORING CLASS IN THE PROJECT