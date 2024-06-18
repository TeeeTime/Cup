package cup.games;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Random;

import net.dv8tion.jda.api.entities.User;

public class Race {
	
	private ArrayList<User> users = new ArrayList<User>();
	
	private LinkedHashMap<String, Integer> field = new LinkedHashMap<String, Integer>();
	
	private String[] userEmojis;
	
	private int bet;
	
	private int secondsToJoin = 20;
	
	public Race(int bet) {
		userEmojis = generateRandomEmojis();
		
		this.bet = bet;
	}
	
	public User randomMove() {
		Random random = new Random();
		
		int index = random.nextInt(users.size());
		
		User user = users.get(index);
		
		int position = field.get(user.getId()) + 1;
		
		field.replace(user.getId(), position);
		
		return user;
	}
	
	public ArrayList<User> getUsers() {
		return users;
	}
	
	public int getPosition(User user) {
		return field.get(user.getId());
	}
	
	public void addUser(User user) {
		users.add(user);
		field.put(user.getId(), 0);
	}
	
	public boolean containsUser(User user) {
		return users.contains(user);
	}
	
	public int getBet() {
		return bet;
	}
	
	public int size() {
		return users.size();
	}
	
	public String getEmoji(int index) {
		return userEmojis[index];
	}

	private String[] generateRandomEmojis() {
		String[] emojis = {":white_circle:", ":red_circle:", ":blue_circle:", ":brown_circle:", ":purple_circle:", ":green_circle:", ":yellow_circle:", ":orange_circle:"};
		
		Random random = new Random();
		
		for(int i = emojis.length - 1; i > 0; i--) {
			int newIndex = random.nextInt(i + 1);
			
			String temp = emojis[newIndex];
			emojis[newIndex] = emojis[i];
			emojis[i] = temp;
		}
		
		return emojis;
	}

	public int getSecondsToJoin() {
		return secondsToJoin;
	}
	
	public void decreaseSecondsToJoin(int seconds) {
		secondsToJoin -= seconds;
	}
	
}

