package cup.discordbot;

import java.awt.Color;
import java.time.Duration;
import java.time.Instant;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class VoiceSessionListener extends ListenerAdapter {
	
	public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
		
		//	Channel joined
		if(event.getChannelJoined() != null && event.getChannelLeft() == null) {
			if(!VoiceSession.voiceSessionExists(event.getChannelJoined().asVoiceChannel())) {
				VoiceSession.addVoiceSession(event.getChannelJoined().asVoiceChannel());
			}
			
			VoiceSession.getVoiceSession(event.getChannelJoined().asVoiceChannel()).userJoined(event.getMember().getUser());
			
		//	Channel left
		}else if(event.getChannelJoined() == null && event.getChannelLeft() != null) {
			
			if(!VoiceSession.voiceSessionExists(event.getChannelLeft().asVoiceChannel())) return;
			
			VoiceSession.getVoiceSession(event.getChannelLeft().asVoiceChannel()).userLeft(event.getMember().getUser());
			
			if(event.getChannelLeft().getMembers().size() == 0) {
				VoiceSession.getVoiceSession(event.getChannelLeft().asVoiceChannel()).getTimer().stop();
				
				String userTimeReport = "";
				
				for(String userId : VoiceSession.getVoiceSession(event.getChannelLeft().asVoiceChannel()).getUserIds()) {
					User user = DiscordBot.INSTANCE.getJDA().retrieveUserById(userId).complete();
					userTimeReport += "**`" + user.getEffectiveName() + "`** `" + formatTime(VoiceSession.getVoiceSession(event.getChannelLeft().asVoiceChannel()).getTimer(user).getCurrentTimeInMillis()) + "`\n";
				}
				
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(Color.YELLOW);
				eb.setTitle("📊 Session Summary");
				
				eb.setDescription("**Duration:**\n`" + formatTime(VoiceSession.getVoiceSession(event.getChannelLeft().asVoiceChannel()).getTimer().getCurrentTimeInMillis()) + "`\n\n**Users:**\n" + userTimeReport);
				
				eb.setTimestamp(Instant.now());
				event.getChannelLeft().asGuildMessageChannel().sendMessageEmbeds(eb.build()).queue();
				
				VoiceSession.removeVoiceSession(event.getChannelLeft().asVoiceChannel());
			}
			
		//	Channel switched
		}else if(event.getChannelJoined() != null && event.getChannelLeft() != null) {
			if(VoiceSession.voiceSessionExists(event.getChannelLeft().asVoiceChannel())) {
				VoiceSession.getVoiceSession(event.getChannelLeft().asVoiceChannel()).userLeft(event.getMember().getUser());
			}
			
			if(!VoiceSession.voiceSessionExists(event.getChannelJoined().asVoiceChannel())) {
				VoiceSession.addVoiceSession(event.getChannelJoined().asVoiceChannel());
			}
			
			VoiceSession.getVoiceSession(event.getChannelJoined().asVoiceChannel()).userJoined(event.getMember().getUser());
			
		}
		
	}
	
	private String formatTime(long millis) {
		if (millis == 0) return "0s";

	    Duration duration = Duration.ofMillis(millis);
	    
	    long days = duration.toDays();
	    long hours = duration.toHoursPart();   
	    long minutes = duration.toMinutesPart();
	    long seconds = duration.toSecondsPart();
	    
	    StringBuilder sb = new StringBuilder();
	    
	    if (days > 0) {
	        sb.append(days).append("d ");
	    }
	    if (hours > 0) {
	        sb.append(hours).append("h ");
	    }
	    if (minutes > 0) {
	        sb.append(minutes).append("m ");
	    }
	    if (seconds > 0) {
	        sb.append(seconds).append("s");
	    }
	    
	    return sb.toString().trim();
	}

}
