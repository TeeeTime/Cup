package cup.util;

public class Stock {
	
	private String ticker;
	private String name;
	private String price;
	private String dayHigh;
	private String dayLow;
	private String yearHigh;
	private String yearLow;
	private String volume;
	
	public Stock(String ticker, String name, String price, String dayHigh, String dayLow, String yearHigh, String yearLow, String volume) {
		this.ticker = ticker;
		this.name = name;
		this.price = price;
		this.dayHigh = dayHigh;
		this.dayLow = dayLow;
		this.yearHigh = yearHigh;
		this.yearLow = yearLow;
		this.volume = volume;
	}

	public String getTicker() {
		return ticker;
	}

	public String getName() {
		return name;
	}

	public String getPrice() {
		return price;
	}

	public String getDayHigh() {
		return dayHigh;
	}

	public String getDayLow() {
		return dayLow;
	}

	public String getYearHigh() {
		return yearHigh;
	}

	public String getYearLow() {
		return yearLow;
	}

	public String getVolume() {
		return volume;
	}
	


}
