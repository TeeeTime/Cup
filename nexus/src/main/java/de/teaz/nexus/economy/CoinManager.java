package main.java.de.teaz.nexus.economy.economy;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import main.java.de.teaz.nexus.database.LiteSQL;
import main.java.de.teaz.nexus.discord.DiscordBot;

import main.java.de.teaz.nexus.economy.LeaderboardEntry;
import net.dv8tion.jda.api.entities.User;

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
	
	public static List<LeaderboardEntry> getLeaderboard() {
		List<main.java.de.teaz.nexus.economy.economy.LeaderboardEntry> leaderboard = new ArrayList<>();
		
		try {
			ResultSet results = LiteSQL.onQuery("SELECT userid, balance FROM coins ORDER BY balance desc LIMIT 10");
			
			int rank = 1;
			
			while(results.next()) {
				User user = DiscordBot.INSTANCE.getJDA().retrieveUserById(Long.parseLong(results.getString("userid"))).complete();
				int balance = results.getInt("balance");
				
				leaderboard.add(new main.java.de.teaz.nexus.economy.economy.LeaderboardEntry(rank, user.getName(), balance, user.getAvatarUrl()));
				
				rank++;
			}	
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
		return leaderboard;
	}
	
}
