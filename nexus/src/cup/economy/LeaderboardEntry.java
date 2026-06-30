package cup.economy;

public class LeaderboardEntry {
	
	private int rank;
	private String name;
	private int balance;
	private String avatarUrl;
	
	public LeaderboardEntry(int rank, String name, int balance, String avatarUrl) {
		this.rank = rank;
        this.name = name;
        this.balance = balance;
        this.avatarUrl = avatarUrl;
	}
	
	public int getRank() {
		return rank;
	}
	
	public String getName() {
		return name;
	}
	
	public int getBalance() {
		return balance;
	}
	
	public String getAvatarUrl() {
		return avatarUrl;
	}

}
