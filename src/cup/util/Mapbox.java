package cup.util;

import java.util.Locale;

public class Mapbox {
	
	public String getLocationMapURL(double latitude, double longitude, int zoom) {
		
		String username = "mapbox";
		
		String styleId = "streets-v12";
		
		String accessToken = "pk.eyJ1IjoidGVlZXRpbWUiLCJhIjoiY21pcGIxbzE1MDVncTNncGo5azJhdXVwaSJ9.Z4YLtq-cH9kq4mWU3E9o_A";
		
		String overlay = String.format(Locale.US, "pin-l-%s%%2Bff0000(%f,%f)", "marker", longitude, latitude);
		
		
		String url = String.format(Locale.US,
				"https://api.mapbox.com/styles/v1/%s/%s/static/%s/%f,%f,%d,0/600x400?access_token=%s",
				username,
				styleId,
				overlay,
				longitude,
				latitude,
				zoom,
				accessToken);
		
		return url;
	}
}
