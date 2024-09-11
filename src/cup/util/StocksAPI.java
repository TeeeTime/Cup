package cup.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StocksAPI {
	
	private String token;
	
	public StocksAPI(String token) {
		this.token = token;
	}
	
	public Stock getStock(String search) {
		try {
			URL url = new URL(" https://api.stockdata.org/v1/data/quote?symbols=" + search.trim() + "&api_token=" + token);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("Content-Type", "application/json");
			con.setConnectTimeout(5000);
			con.setReadTimeout(5000);
			HttpURLConnection.setFollowRedirects(true);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			
			String inputLine;
			StringBuffer content = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			
			in.close();
			
			con.disconnect();
			
			String returned = content.substring(content.indexOf("\"returned\":") + "\"returned\":".length());
			returned = returned.substring(0, returned.indexOf(","));
			returned = returned.replace("\"", "").trim();
			
			if(returned.equals("0")) return null;
			
			String ticker = content.substring(content.indexOf("\"ticker\":") + "\"ticker\":".length());
			ticker = ticker.substring(0, ticker.indexOf(","));
			ticker = ticker.replace("\"", "").trim();
			
			String name = content.substring(content.indexOf("\"name\":") + "\"name\":".length());
			name = name.substring(0, name.indexOf(","));
			name = name.replace("\"", "").trim();
			
			String price = content.substring(content.indexOf("\"price\":") + "\"price\":".length());
			price = price.substring(0, price.indexOf(","));
			price = price.replace("\"", "").trim();
			
			String dayHigh = content.substring(content.indexOf("\"day_high\":") + "\"day_high\":".length());
			dayHigh = dayHigh.substring(0, dayHigh.indexOf(","));
			dayHigh = dayHigh.replace("\"", "").trim();
			
			String dayLow = content.substring(content.indexOf("\"day_low\":") + "\"day_low\":".length());
			dayLow = dayLow.substring(0, dayLow.indexOf(","));
			dayLow = dayLow.replace("\"", "").trim();
			
			String yearHigh = content.substring(content.indexOf("\"52_week_high\":") + "\"52_week_high\":".length());
			yearHigh = yearHigh.substring(0, yearHigh.indexOf(","));
			yearHigh = yearHigh.replace("\"", "").trim();
			
			String yearLow = content.substring(content.indexOf("\"52_week_low\":") + "\"52_week_low\":".length());
			yearLow = yearLow.substring(0, yearLow.indexOf(","));
			yearLow = yearLow.replace("\"", "").trim();
			
			String volume = content.substring(content.indexOf("\"volume\":") + "\"volume\":".length());
			volume = volume.substring(0, volume.indexOf(","));
			volume = volume.replace("\"", "").trim();
			
			Stock stock = new Stock(ticker, name, price, dayHigh, dayLow, yearHigh, yearLow, volume);
			
			return stock;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

}
