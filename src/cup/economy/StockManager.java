package cup.economy;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import cup.database.LiteSQL;
import net.dv8tion.jda.api.entities.User;

public class StockManager {
	
	public static int secondsTillPriceChange = 0;
	
	public void addStock(String id, int price, String emoji) {
		if(exists(id)) return;
		
		LiteSQL.onUpdate("INSERT INTO stocks(stockid, price, trend, emoji) VALUES('" + id + "', " + price + ", '" + generatePriceTrend() + "', '" + emoji + "')");
	}
	
	public void startScheduler(int intervalInSeconds) {
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				secondsTillPriceChange = intervalInSeconds;
				for(Stock stock : getStocks()) {
					String[] trend = stock.getTrend().split("-");
					
					int change = bottomHeavyRandom(true, 63, 3);
					
					if(trend[0].equals("i")) {
						LiteSQL.onUpdate("UPDATE stocks SET price = " + (stock.getPrice() + change) + " WHERE stockid = '" + stock.getId() + "'");
						
					}else if(trend[0].equals("d")) {
						if(stock.getPrice() - change <= 0) {
							LiteSQL.onUpdate("UPDATE stocks SET trend = '" + generatePriceTrend() + "' WHERE stockid = '" + stock.getId() + "'");
							continue;
						}else {
							LiteSQL.onUpdate("UPDATE stocks SET price = '" + (stock.getPrice() - change) + "' WHERE stockid = '" + stock.getId() + "'");
						}
					}
					
					int trendDuration = 1;
					
					try {
						trendDuration = Integer.parseInt(trend[1]);
					} catch(NumberFormatException e) {
						e.printStackTrace();
					}
					
					trendDuration--;
					if(trendDuration == 0) {
						LiteSQL.onUpdate("UPDATE stocks SET trend = '" + generatePriceTrend() + "' WHERE stockid = '" + stock.getId() + "'");
					}else {
						LiteSQL.onUpdate("UPDATE stocks SET trend = '" + trend[0] + "-" + trendDuration + "' WHERE stockid = '" + stock.getId() + "'");
					}
				}
			}
		
		}, 1000 * intervalInSeconds, 1000 * intervalInSeconds);
		
		Timer readableTimer = new Timer();
		
		readableTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				secondsTillPriceChange--;
			}
		}, 100, 1000);
	}
	
	private int bottomHeavyRandom(boolean positive, int max, double bias) {
		Random random = new Random();
		double v = Math.pow(random.nextDouble(), bias); 
		int result = 1 + (int)(v * max);
		if(positive) {
			return result;
		}else {
			return result * (-1);
		}
	}
	
	public String generatePriceTrend() {
		String output = "";
		
		Random random = new Random();
		
		switch(random.nextInt(3)) {
		case 0: output += "i"; break;
		case 1: output += "d"; break;
		case 2: output += "c"; break;
		}
		
		int length = bottomHeavyRandom(true, 7, 3);
		
		output += "-" + length;
		
		return output;
	}
	
	public int getStockAmount(User user, String stockId) {
		if(entryExists(user, stockId)) {
			ResultSet results = LiteSQL.onQuery("SELECT amount FROM stockholders WHERE userid = '" + user.getId() + "' AND stockid = '" + stockId + "'");
			
			try {
				if(results.next()) {
					return results.getInt("amount");
				}
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	public void setStockAmount(User user, String stockId, int amount) {
		if(!entryExists(user, stockId)) {
        	LiteSQL.onUpdate("INSERT INTO stockholders(userid, stockid, amount) VALUES('" + user.getId() + "', '" + stockId + "', " + amount + ")");
        }else {
        	LiteSQL.onUpdate("UPDATE stockholders SET amount = '" + amount + "' WHERE userid = '" + user.getId() + "' AND stockid = '" + stockId + "'");
        }
	}
	
	
	/*
	 * Returns all Stocks owned by the user
	 */
	public LinkedHashMap<Stock, Integer> getPortfolio(User user){
		ResultSet countResults = LiteSQL.onQuery("SELECT COUNT(stockid) AS c FROM stockholders WHERE userid = '" + user.getId() + "'");
		
		int count = 0;
		
		try {
			count = countResults.getInt("c");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		ResultSet results = LiteSQL.onQuery("SELECT stockid FROM stockholders WHERE userid = '" + user.getId() + "'");
		
		List<String> stockIds = new ArrayList<String>();
		
		for(int i = 0; i < count; i++) {
			try {
				if(results.next()) {
					stockIds.add(results.getString("stockid"));
				}
			} catch (SQLException e) {
				System.out.println("[Portfolio] An error occurred while retrieving stock ids from database");
			}
		}
		
		LinkedHashMap<Stock, Integer> portfolio = new LinkedHashMap<>();
		
		for(String id : stockIds) {
			int amount = getStockAmount(user, id);
		    Stock stock = getStock(id);
		    if(amount > 0) portfolio.put(stock, amount);
		}

	    return portfolio;
	}
	
	/*
	 * Checks if User has a database entry for a given stock
	 */
	private boolean entryExists(User user, String stockId) {
		ResultSet results = LiteSQL.onQuery("SELECT amount FROM stockholders WHERE userid = '" + user.getId() + "' AND stockid = '" + stockId + "'");
		
		try {
			if(results.next()) {
				return true;
			}else {
				return false;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Stock getStock(String stockId) {
		ResultSet results = LiteSQL.onQuery("SELECT stockid, price, trend, emoji FROM stocks WHERE stockid = '" + stockId + "'");
		
		Stock stock;
		
		try {
			while(results.next()) {
				stock = new Stock(results.getString("stockid"), results.getInt("price"), results.getString("trend"), results.getString("emoji"));
				
				return stock;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		stock = new Stock("N", 0, "N", "N");
		return stock;
	}
	
	public ArrayList<Stock> getStocks() {
		ResultSet results = LiteSQL.onQuery("SELECT stockid, price, trend, emoji FROM stocks");
		
		ArrayList<Stock> stocks = new ArrayList<Stock>();
		
		try {
			while(results.next()) {
				Stock stock = new Stock(results.getString("stockid"), results.getInt("price"), results.getString("trend"), results.getString("emoji"));
				
				stocks.add(stock);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return stocks;
	}
	
	public int getPrice(String stockId) {
		if(!exists(stockId)) return 0;
		
		ResultSet results = LiteSQL.onQuery("SELECT price FROM stocks WHERE stockid = '" + stockId + "'");
		
		try {
			if(results.next()) {
				return results.getInt("price");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public boolean exists(String stockId) {
		if(stockId.length() != 3) {
			return false;
		}
		
		ResultSet results = LiteSQL.onQuery("SELECT stockid FROM stocks WHERE stockid = '" + stockId + "'");
		
		try {
			if(results.next()) {
				return true;
			}else {
				return false;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
}
