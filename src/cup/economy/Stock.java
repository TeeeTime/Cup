package cup.economy;

public class Stock {
	
	private String id;
	private int price;
	private String trend;
	private String emoji;
	
	public Stock(String id, int price, String trend, String emoji) {
		this.id = id;
		this.price = price;
		this.trend = trend;
		this.emoji = emoji;
	}

	public String getId() {
		return id;
	}

	public int getPrice() {
		return price;
	}

	public String getTrend() {
		return trend;
	}
	
	public String getEmoji() {
		return emoji;
	}

}
