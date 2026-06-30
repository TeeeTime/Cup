package cup.games;

import java.sql.ResultSet;

import cup.database.LiteSQL;
import net.dv8tion.jda.api.entities.User;

public class Stats {
	
	public static int getStat(String statId, User user) {
		if(!entryExists(statId, user)) return 0;
		
		try {
			ResultSet results = LiteSQL.onQuery("SELECT value FROM stats WHERE statid = '" + statId + "' AND userid = '" + user.getId() + "'");
		
			if(results.next()) {
				return results.getInt("value");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return 0;
	}
	
	public static void incrementStat(String statId, User user) {
		try {
			if(!entryExists(statId, user)) {
	        	LiteSQL.onUpdate("INSERT INTO stats(statid, userid, value) VALUES('" + statId + "', '" + user.getId() + "', 1)");
	        }else {
	        	LiteSQL.onUpdate("UPDATE stats SET value = " + (getStat(statId, user) + 1) + " WHERE statid = '" + statId + "' AND userid = '" + user.getId() + "'");
	        }
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean entryExists(String statId, User user) {
		try {
			ResultSet results = LiteSQL.onQuery("SELECT value FROM stats WHERE statid = '" + statId + "' AND userid = '" + user.getId() + "'");
		
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
