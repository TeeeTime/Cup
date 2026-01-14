package cup.economy;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import cup.database.LiteSQL;

public class DailyManager {
	
	private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	
	public boolean redeemable(String userId) {
		if(!entryExists(userId)) {
			return true;
		}
		
		if(!getCurrentDate().equals(getLastDate(userId))) {
			return true;
		}else {
			return false;
		}
	}
	
	public void redeem(String userId) {
		if(redeemable(userId)) {
			try {
				if(!entryExists(userId)) {
					LiteSQL.onUpdate("INSERT INTO daily(userid, lastclaimdate, streak) VALUES(" + userId + ", '" + getCurrentDate() + "', " + 1 + ")");
					CoinManager.setCoins(userId, CoinManager.getCoins(userId) + 500);
				}else {
					if(calculateDayDifference(userId) == 1) {
						LiteSQL.onUpdate("UPDATE daily SET lastclaimdate = '" + getCurrentDate() + "', streak = " + (getStreak(userId) + 1) + " WHERE userid = " + userId);
						
						if((getStreak(userId) % 7) == 0) {
							CoinManager.setCoins(userId, CoinManager.getCoins(userId) + 1000);
						}else {
							CoinManager.setCoins(userId, CoinManager.getCoins(userId) + 500);
						}
					}else {
						LiteSQL.onUpdate("UPDATE daily SET lastclaimdate = '" + getCurrentDate() + "', streak = " + 1 + " WHERE userid = " + userId);
						CoinManager.setCoins(userId, CoinManager.getCoins(userId) + 500);
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getStreak(String userId) {
		try {
			if(!(calculateDayDifference(userId) > 1)) {
				ResultSet results = LiteSQL.onQuery("SELECT streak FROM daily WHERE userid = " + userId);
				
				if(results.next()) {
					return results.getInt("streak");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	private String getLastDate(String userId) {
		try {
			ResultSet results = LiteSQL.onQuery("SELECT lastclaimdate FROM daily WHERE userid = " + userId);
			
			if(results.next()) {
				return results.getString("lastclaimdate");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return getCurrentDate();
	}
	
	private String getCurrentDate() {
		return LocalDate.now().format(DATE_FORMAT);
	}
	
	private boolean entryExists(String userId) {
		
		try {
			ResultSet results = LiteSQL.onQuery("SELECT lastclaimdate FROM daily WHERE userid = " + userId);
			if(results.next()) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private int calculateDayDifference(String userId) {
		LocalDate currentDate = LocalDate.now();
		LocalDate lastDate = LocalDate.parse(getLastDate(userId), DATE_FORMAT);
		
		return (int) ChronoUnit.DAYS.between(lastDate, currentDate);
	}
}
