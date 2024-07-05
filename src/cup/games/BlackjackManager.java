package cup.games;

import java.util.HashMap;

public class BlackjackManager {

	private HashMap<String, Blackjack> blackjacks = new HashMap<String, Blackjack>();
	
	public void addBlackjack(String userId, Blackjack blackjack) {
		blackjacks.put(userId, blackjack);
	}
	
	public boolean contains(String userId) {
		return blackjacks.containsKey(userId);
	}
	
	public void removeBlackjack(String userId) {
		blackjacks.remove(userId);
	}
	
	public Blackjack getBlackjack(String userId) {
		return blackjacks.get(userId);
	}
}
