package cup.website.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import cup.economy.CoinManager;
import cup.economy.DailyManager;
import cup.economy.LeaderboardEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class EconomyService {
	
	private List<LeaderboardEntry> cachedLeaderboard = new ArrayList<>();
	
	public int getBalance(String discordId) {
        return CoinManager.getCoins(discordId);
	}
	
	public void setBalance(String discordId, int amount) {
		CoinManager.setCoins(discordId, amount);
	}
	
	public List<LeaderboardEntry> getLeaderboard() {	
		if (cachedLeaderboard.isEmpty()) {
            updateLeaderboardCache();
        }
        return cachedLeaderboard;
	}
	
	@Scheduled(fixedRate = 2, timeUnit = TimeUnit.MINUTES)
	public void updateLeaderboardCache() {
		try {
            List<LeaderboardEntry> freshData = CoinManager.getLeaderboard();
            
            if (freshData != null && !freshData.isEmpty()) {
                this.cachedLeaderboard = freshData;
                System.out.println("[Economy] Leaderboard cache updated.");
            }
        } catch (Exception e) {
        	System.out.println("[Economy] ⚠️ Could not update leaderboard from Discord: " + e.getMessage());
            System.out.println("[Economy] Serving cached data (" + cachedLeaderboard.size() + " entries) until next retry.");
        }
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