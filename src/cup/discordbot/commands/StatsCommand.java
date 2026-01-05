package cup.discordbot.commands;

import java.text.DecimalFormat;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.games.Stats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StatsCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length > 2) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		User user = event.getAuthor();
		
		if(args.length == 2) {
			if(event.getMessage().getMentions().getMembers().size() == 1) {
				user = event.getMessage().getMentions().getMembers().get(0).getUser();
			}else {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
				return;
			}
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(DiscordBot.EMBEDCOLOR);
		eb.setTitle(user.getName() + "'s stats");
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		int racesPlayed = Stats.getStat("racesPlayed", user);
		int racesWon = Stats.getStat("racesWon", user);
		
		if(racesPlayed < 1) {
			eb.addField("Race", "You didn't participate in any races yet!", false);
		}else {
			float winPercentage = ((float) racesWon / (float) racesPlayed) * 100;
			
			eb.addField("Race", "`Played:` " + racesPlayed + "\n"
					+ "`Won:` " + racesWon + " (" + df.format(winPercentage) + "%)", false);
		}
		
		int rpsPlayed = Stats.getStat("rpsPlayed", user);
		int rpsWon = Stats.getStat("rpsWon", user);
		int rpsTied = Stats.getStat("rpsTied", user);
		int rpsLost = Stats.getStat("rpsLost", user);
		
		if(rpsPlayed < 1) {
			eb.addField("Rock Paper Scissors", "You didn't play any games of rock paper scissors yet!", false);
		}else {
			float winPercentage = ((float) rpsWon / (float) rpsPlayed) * 100;
			float tiedPercentage = ((float) rpsTied / (float) rpsPlayed) * 100;
			float lostPercentage = ((float) rpsLost / (float) rpsPlayed) * 100;
			
			eb.addField("Rock Paper Scissors", "`Played:` " + rpsPlayed + "\n"
					+ "`Won:` " + rpsWon + " (" + df.format(winPercentage) + "%)\n"
					+ "`Tied:` " + rpsTied + " (" + df.format(tiedPercentage) + "%)\n"
					+ "`Lost:` " + rpsLost + " (" + df.format(lostPercentage) + "%)", false);
		}
		
		int blackjackPlayed = Stats.getStat("blackjackPlayed", user);
		int blackjackWon = Stats.getStat("blackjackWon", user);
		int blackjackTied = Stats.getStat("blackjackTied", user);
		int blackjackLost = Stats.getStat("blackjackLost", user);
		
		System.out.println(blackjackWon);
		
		if(blackjackPlayed < 1) {
			eb.addField("Blackjack", "You didn't play any games of blackjack yet!", false);
		}else {
			float winPercentage = ((float) blackjackWon / (float) blackjackPlayed) * 100;
			float tiedPercentage = ((float) blackjackTied / (float) blackjackPlayed) * 100;
			float lostPercentage = ((float) blackjackLost / (float) blackjackPlayed) * 100;
			
			eb.addField("Blackjack", "`Played:` " + blackjackPlayed + "\n"
					+ "`Won:` " + blackjackWon + " (" + df.format(winPercentage) + "%)\n"
					+ "`Tied:` " + blackjackTied + " (" + df.format(tiedPercentage) + "%)\n"
					+ "`Lost:` " + blackjackLost + " (" + df.format(lostPercentage) + "%)", false);
		}
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "stats";
	}

	@Override
	public String getDescription() {
		return "Look at the stats of your gambling career and cry about it :)";
	}

	@Override
	public boolean isPublic() {
		return true;
	}

}
