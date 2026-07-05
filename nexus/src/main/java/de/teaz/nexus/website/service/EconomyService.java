package de.teaz.nexus.website.service;

import de.teaz.nexus.economy.CoinManager;
import de.teaz.nexus.economy.DailyManager;
import de.teaz.nexus.economy.LeaderboardEntry;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EconomyService {
	
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
		return DailyManager.getStreak(discordId);
	}
	
	public boolean isDailyRedeemable(String discordId) {
		return DailyManager.redeemable(discordId);
	}
	
	public void redeemDaily(String discordId) {
		DailyManager.redeem(discordId);
	}
}