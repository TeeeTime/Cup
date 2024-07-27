package cup.economy;

import java.sql.ResultSet;

import cup.database.LiteSQL;
import net.dv8tion.jda.api.entities.User;

public class CoinManager {
	
	public static int getCoins(User user) {
		try {
			ResultSet results = LiteSQL.onQuery("SELECT balance FROM coins WHERE userid = " + user.getId());
		
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
	
	public static void setCoins(User user, int amount) {
        
        if(!entryExists(user)) {
        	LiteSQL.onUpdate("INSERT INTO coins(userid, balance) VALUES(" + user.getId() + ", " + amount + ")");
        }else {
        	LiteSQL.onUpdate("UPDATE coins SET balance = " + amount + " WHERE userid = " + user.getId());
        }
	}
	
	public static boolean entryExists(User user) {
		try {
        	ResultSet results = LiteSQL.onQuery("SELECT balance FROM coins WHERE userid = " + user.getId());
		
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
