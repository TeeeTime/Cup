package cup.games;

import java.util.HashMap;

public class RaceManager {
	
	private HashMap<String, Race> races = new HashMap<String, Race>();
	
	public void addRace(String userId, Race race) {
		races.put(userId, race);
	}
	
	public boolean contains(String userId) {
		return races.containsKey(userId);
	}
	
	public void removeRace(String userId) {
		races.remove(userId);
	}
	
	public Race getRace(String userId) {
		return races.get(userId);
	}

}
