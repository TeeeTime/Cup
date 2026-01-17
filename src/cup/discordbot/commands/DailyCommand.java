package cup.discordbot.commands;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.DailyManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DailyCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length != 1) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		User user = event.getAuthor();
		
		if(!DailyManager.redeemable(user.getId())) {
			LocalDateTime now = LocalDateTime.now();
            LocalDateTime midnight = LocalDate.now().atTime(LocalTime.MAX);
            
            long secondsUntilMidnight = java.time.Duration.between(now, midnight).getSeconds();
            long hours = secondsUntilMidnight / 3600;
            long minutes = (secondsUntilMidnight % 3600) / 60;
            
            String timeLeft = hours + "h " + minutes + "m";
             
            event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.dailyClaimedEmbed(timeLeft).build()).queue();
            
            return;
		}
		
		DailyManager.redeem(user.getId());
		
		int streak = DailyManager.getStreak(user.getId());
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(DiscordBot.EMBEDCOLOR);
		if(streak % 7 == 0) {
			eb.addField("Daily bonus claimed!", "You received 500 :coin: + 500 :coin: streak reward\n\nYou are on a " + streak + " day streak :fire:", false);
		}else {
			eb.addField("Daily bonus claimed!", "You received 500 :coin:\n\nYou are on a " + streak + " day streak :fire:", false);
		}
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "daily";
	}

	@Override
	public String getDescription() {
		return "Claim 500 :coin: daily. Resets at 12 PM GMT+2. Every 7 days you can keep a streak, you will receive a 500 :coin: reward.";
	}

	@Override
	public boolean isPublic() {
		return true;
	}

}
