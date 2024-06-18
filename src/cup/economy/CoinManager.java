package cup.economy;

import java.sql.ResultSet;
import java.sql.SQLException;

import cup.database.LiteSQL;
import net.dv8tion.jda.api.entities.User;

public class CoinManager {
	
	public static int getCoins(User user) {
		
		ResultSet results = LiteSQL.onQuery("SELECT balance FROM coins WHERE userid = " + user.getId());
		
		try {
			if(results.next()) {
				return results.getInt("balance");
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static ResultSet getTable() {
		
		ResultSet results = LiteSQL.onQuery("SELECT * FROM coins");
		
		return results;
	}
	
	public static void setCoins(User user, int amount) {
        
        if(!entryExists(user)) {
        	LiteSQL.onUpdate("INSERT INTO coins(userid, balance) VALUES(" + user.getId() + ", " + amount + ")");
        }else {
        	LiteSQL.onUpdate("UPDATE coins SET balance = " + amount + " WHERE userid = " + user.getId());
        }
	}
	
	public static boolean entryExists(User user) {
		
        ResultSet results = LiteSQL.onQuery("SELECT balance FROM coins WHERE userid = " + user.getId());
		
		try {
			if(results.next()) {
				return true;
			}else {
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;		
	}
	
}
