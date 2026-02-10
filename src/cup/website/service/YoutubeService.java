package cup.website.service;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.VideoListResponse;

@Service
public class YoutubeService {
	
	@Value("${youtube.token}")
	private String token;
	
	public long getVideoDuration(String videoId) throws IOException {
	    YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), null)
	            .setApplicationName("TEAZ")
	            .build();

	    YouTube.Videos.List request = youtube.videos()
	            .list(Collections.singletonList("contentDetails"));
	    
	    VideoListResponse response = request.setId(Collections.singletonList(videoId))
	            .setKey(token)
	            .execute();

	    String isoDuration = response.getItems().get(0).getContentDetails().getDuration();
	    return java.time.Duration.parse(isoDuration).getSeconds();
	}
	
	public String extractVideoId(String url) {
	    if (url == null || url.isBlank()) return null;

	    // Regex to capture the 11-character ID from various YouTube URL formats
	    String pattern = "(?i)(?:youtube\\.com/(?:[^/]+/.+/|(?:v|e(?:mbed)?)/|.*[?&]v=)|youtu\\.be/|youtube\\.com/shorts/)([a-zA-Z0-0_-]{11})";
	    
	    java.util.regex.Pattern compiledPattern = java.util.regex.Pattern.compile(pattern);
	    java.util.regex.Matcher matcher = compiledPattern.matcher(url);

	    if (matcher.find()) {
	        return matcher.group(1);
	    }
	    return null;
	}

}
