package cup.ai;

import java.util.ArrayList;
import java.util.List;

public class Response {
	
	private String text;
	
	private List<String> imageURLs;
	
	public Response(String text) {
		this.setText(text);
		imageURLs = new ArrayList<String>();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<String> getImageURLs() {
		return imageURLs;
	}

	public void addImageURL(String image_url) {
		this.imageURLs.add(image_url);
	}

}
