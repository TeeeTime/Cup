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
		return CoinManager.getLeaderboard();
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