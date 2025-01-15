package cup.util;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class CounterstrikeBlog {
    
    public String extractChangelog(String steamUrl) {
        try {
            // Convert the announcement URL to the JSON endpoint
            String eventId = steamUrl.split("/detail/")[1].split("\\?")[0];
            String jsonUrl = "https://api.steampowered.com/ISteamNews/GetNewsForApp/v2/?appid=730&newsid=" + eventId;
            
            // Make HTTP request
            URL url = new URL(jsonUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            // Read the response
            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                        conn.getResponseCode() >= 400 ? conn.getErrorStream() : conn.getInputStream(), 
                        StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }
            
            if (response.toString().isEmpty()) {
                return "No content received from Steam API";
            }
            
            // Parse the response to get just the first news item's contents
            String jsonResponse = response.toString();
            int contentsStart = jsonResponse.indexOf("\"contents\":\"") + "\"contents\":\"".length();
            int contentsEnd = jsonResponse.indexOf("\",\"feedlabel\"");
            
            if (contentsStart == -1 || contentsEnd == -1) {
                return "Could not find changelog content in response";
            }
            
            String content = jsonResponse.substring(contentsStart, contentsEnd)
                    .replaceAll("\\\\n", "\n")
                    .replaceAll("\\\\r", "")
                    .replaceAll("\\\\\"", "\"")
                    // Convert bullet points to Discord format
                    .replaceAll("\\[\\*\\]", "- ")
                    // Convert BBCode italics to Discord italics
                    .replaceAll("\\[i\\]", "_")
                    .replaceAll("\\[/i\\]", "_")
                    // Remove list tags
                    .replaceAll("\\[/?list\\]", "")
                    // Format headers with Discord bold
                    .replaceAll("\\[(.*?)\\]", "**[$1]**")
                    .replaceAll("(?m)^\\s+$", "")
                    .replaceAll("\n{3,}", "\n\n")
                    .trim();
            
            return content;
            
        } catch (Exception e) {
            return "Error extracting changelog: " + e.getMessage();
        }
    }
}
