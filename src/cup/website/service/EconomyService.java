package cup.website.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cup.discordbot.DiscordBot;
import cup.economy.CoinManager;
import cup.economy.DailyManager;
import cup.economy.LeaderboardEntry;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class EconomyService {
	
	@Value("${app.db-path}")
    private String dbUrl;
	
	public int getBalance(String discordId) {
        return CoinManager.getCoins(discordId);
	}
	
	public void setBalance(String discordId, int amount) {
		CoinManager.setCoins(discordId, amount);
	}
	
	public List<LeaderboardEntry> getLeaderboard() {
		String query = "SELECT userid, balance FROM coins ORDER BY balance desc LIMIT 10";
		
		List<LeaderboardEntry> leaderboard = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection(dbUrl);
			PreparedStatement statement = connection.prepareStatement(query)) {
			
			ResultSet results = statement.executeQuery();
			
			int rank = 1;
			
			while(results.next()) {
				User user = DiscordBot.INSTANCE.getJDA().retrieveUserById(Long.parseLong(results.getString("userid"))).complete();
				
				leaderboard.add(new LeaderboardEntry(rank, user.getName(), getBalance(user.getId()), user.getAvatarUrl()));
				
				rank++;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return leaderboard;
	}
	
	public int getStreak(String discordId) {
		DailyManager dailyManager = new DailyManager();
		return dailyManager.getStreak(discordId);
	}
	
	public boolean isDailyRedeemable(String discordId) {
		DailyManager dailyManager = new DailyManager();
		return dailyManager.redeemable(discordId);
	}
	
	public void redeemDaily(String discordId) {
		DailyManager dailyManager = new DailyManager();
		dailyManager.redeem(discordId);
	}
}