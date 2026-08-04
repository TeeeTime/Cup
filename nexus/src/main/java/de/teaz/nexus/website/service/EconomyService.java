package de.teaz.nexus.website.service;

import de.teaz.nexus.dto.DailyRewardStatus;
import de.teaz.nexus.dto.LeaderboardEntry;
import de.teaz.nexus.economy.CoinManager;
import de.teaz.nexus.economy.DailyManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EconomyService {

    private final CoinManager coinManager;
    private final DailyManager dailyManager;

    @Autowired
    public EconomyService(CoinManager coinManager, DailyManager dailyManager) {
        this.coinManager = coinManager;
        this.dailyManager = dailyManager;
    }

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

    public DailyRewardStatus getDailyStatus(String discordId) {
        int streak = DailyManager.getStreak(discordId);
        DailyRewardStatus status = new DailyRewardStatus(
                DailyManager.redeemable(discordId),
                DailyManager.getCooldownTimeLeft(discordId),
                streak,
                ((streak / 7) + 1) * 7
        );

        return status;
    }
}