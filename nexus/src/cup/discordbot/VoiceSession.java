package cup.discordbot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cup.util.Timer;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;

public class VoiceSession {
	
	//channelid -> VoiceSession
	private static HashMap<String, VoiceSession> sessions = new HashMap<String, VoiceSession>();
	
	//userid -> Timer
	private HashMap<String, Timer> users = new HashMap<String, Timer>();
	
	private Timer sessionTimer;
	
	public VoiceSession() {
		sessionTimer = new Timer();
		sessionTimer.start();
	}
	
	public static void addVoiceSession(VoiceChannel channel) {
		if(!voiceSessionExists(channel)) {
			sessions.put(channel.getId(), new VoiceSession());
		}
	}
	
	public static VoiceSession getVoiceSession(VoiceChannel channel) {
		if(!voiceSessionExists(channel)) return null;
		
		return sessions.get(channel.getId());
	}
	
	public static void removeVoiceSession(VoiceChannel channel) {
		if(!voiceSessionExists(channel)) return;
		
		sessions.remove(channel.getId());
	}
	
	public static boolean voiceSessionExists(VoiceChannel channel) {
		return sessions.containsKey(channel.getId());
	}
	
	public Timer getTimer() {
		return sessionTimer;
	}
	
	public void userJoined(User user) {
		if(!userExists(user)) {
			Timer timer = new Timer();
			timer.start();
			users.put(user.getId(), timer);
		}else {
			users.get(user.getId()).start();
		}
	}
	
	public void userLeft(User user) {
		if(!userExists(user)) return;
		
		users.get(user.getId()).stop();
	}
	
	public Timer getTimer(User user) {
		if(!userExists(user)) return null;
		
		return users.get(user.getId());
	}
	
	public List<String> getUserIds() {
		return new ArrayList<>(users.keySet());
	}
	
	public boolean userExists(User user) {
		return users.containsKey(user.getId());
	}

}
