package cup.discordbot.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ProfileCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length > 2) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		User user;
		Member member;
		
		if(args.length == 1) {
			 user = event.getAuthor();
			 member = event.getMember();
			
		}else {
			if(event.getMessage().getMentions().getMembers().size() != 1) {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
				return;
			}
			
			member = event.getMessage().getMentions().getMembers().get(0);
			user = member.getUser();
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.ORANGE);
		eb.setTitle(user.getName() + "'s profile");
		eb.setThumbnail(user.getAvatarUrl());
		
		String bannerURL = user.retrieveProfile().complete().getBannerUrl();
		
		if(bannerURL != null) {
			eb.setImage(bannerURL);
		}
		
		int coins = CoinManager.getCoins(user);
		if(coins > 0) eb.addField("Coins:", coins + " :coin:", true);
		
		eb.addField("Account created:", "<t:" + user.getTimeCreated().toEpochSecond() + ":d>", true);
		
		String onlineStatus = "";
		
		switch(member.getOnlineStatus()) {
		case ONLINE: onlineStatus = ":green_circle: Online"; break;
		case IDLE: onlineStatus = ":orange_circle: AFK"; break;
		case DO_NOT_DISTURB: onlineStatus = ":red_circle: Do not disturb"; break;
		case OFFLINE: onlineStatus = ":black_circle: Offline"; break;
		case INVISIBLE: onlineStatus = ":black_circle: Offline"; break;
		case UNKNOWN: onlineStatus = ":black_circle: Offline"; break;
		}
		eb.addField("Status:", onlineStatus, true);
		List<Activity> activities = new ArrayList<Activity>();
		if(event.getMember().getActivities() != null) {
			activities = member.getActivities();
		}
		
		if(activities.size() > 0) {
			String activitiesAsString = "";
			
			for(Activity activity : activities) {
				switch(activity.getType()) {
				case PLAYING: activitiesAsString += "* Playing " + activity.getName() + "\n"; break;
				case LISTENING: activitiesAsString += "* Listening to " + activity.getState().replace("; ", " & ") + "\n"; break;
				case WATCHING: activitiesAsString += "* Watching " + activity.getName() + "\n"; break;
				case STREAMING: activitiesAsString += "* Streaming " + activity.getState() + "[LINK](" + activity.getUrl() + ")" + "\n"; break;
				case CUSTOM_STATUS: activitiesAsString += "* " + activity.getEmoji().getFormatted() + " " + activity.getName() + "\n"; break;
				default: break;
				}
			}
			
			eb.addField("Activity:", activitiesAsString, false);
		}
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "profile <user>";
	}

	@Override
	public String getDescription() {
		return "Look at your or other peoples profile.";
	}

	@Override
	public boolean isPublic() {
		return true;
	}

}
