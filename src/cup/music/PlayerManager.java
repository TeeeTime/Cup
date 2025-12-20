package cup.music;

import java.util.HashMap;
import java.util.Map;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;


public class PlayerManager {
	
    private final AudioPlayerManager playerManager;
    private final Map<String, AudioPlayer> players;
	
	public PlayerManager() {
		this.playerManager = new DefaultAudioPlayerManager();
        this.players = new HashMap<>();
        
        AudioSourceManagers.registerRemoteSources(playerManager);
	}
	
	public void loadAndPlay(Guild guild, VoiceChannel channel, String URL) {
		
		AudioPlayer player = players.computeIfAbsent(guild.getId(), (id) -> playerManager.createPlayer());
		
		player.setVolume(70);
		
		AudioManager audioManager = guild.getAudioManager();
		
		if(!audioManager.isConnected()) {
            audioManager.openAudioConnection(channel);
            audioManager.setSendingHandler(new AudioPlayerSendHandler(player));
        }
		
		playerManager.loadItem(URL, new AudioLoadResultHandler() {
			@Override
            public void trackLoaded(AudioTrack track) {
                player.playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                player.playTrack(playlist.getTracks().get(0));
            }
            
            @Override
            public void noMatches() {
                System.out.println("Nothing found by " + URL);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.err.println("Could not play: " + exception.getMessage());
            }
        });
		
	}
	
	public void stopAndClear(Guild guild) {
	    AudioPlayer player = players.remove(guild.getId());
	    
	    AudioManager audioManager = guild.getAudioManager();
	    
	    if (player != null) {
	        player.stopTrack();
	        
	        player.destroy();
	    }
	    
	    if(audioManager.isConnected()) {
	    	audioManager.closeAudioConnection();
	    }
	}
	
	public AudioPlayer getPlayer(Guild guild) {
		if(players.containsKey(guild.getId())) {
			return players.get(guild.getId());
		}else {
			return null;
		}
	}

}
