package cup.economy;

import java.sql.ResultSet;

import cup.database.LiteSQL;

public class CoinManager {
	
	public static int getCoins(String userId) {
		try {
			ResultSet results = LiteSQL.onQuery("SELECT balance FROM coins WHERE userid = " + userId);
		
			if(results.next()) {
				return results.getInt("balance");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static ResultSet getTable() {
		
		ResultSet results;
		try {
			results = LiteSQL.onQuery("SELECT * FROM coins");
			return results;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void setCoins(String userId, int amount) {
        try {
        	if(!entryExists(userId)) {
        		LiteSQL.onUpdate("INSERT INTO coins(userid, balance) VALUES(" + userId + ", " + amount + ")");
        	}else {
        		LiteSQL.onUpdate("UPDATE coins SET balance = " + amount + " WHERE userid = " + userId);
        	}
        }catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	public static boolean entryExists(String userId) {
		try {
        	ResultSet results = LiteSQL.onQuery("SELECT balance FROM coins WHERE userid = " + userId);
		
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
	
}
