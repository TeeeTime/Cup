package de.teaz.nexus.discord.commands;

import java.util.List;

import de.teaz.nexus.discord.Command;
import de.teaz.nexus.discord.DiscordBot;
import de.teaz.nexus.discord.ErrorEmbedBuilder;
import de.teaz.nexus.dto.LeaderboardEntry;
import de.teaz.nexus.economy.CoinManager;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class LeaderboardCommand implements Command {

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length != 1) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		String output = "";
		
		List<LeaderboardEntry> users = CoinManager.getLeaderboard();
		
		for(int i = 0; i < users.size(); i++) {
			output += "**`" + users.get(i).rank() + ".`** `" + users.get(i).name() + "` (" + users.get(i).balance() + " :coin:)\n";
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		
		eb.setColor(DiscordBot.EMBEDCOLOR);
		eb.addField("🏆 Leaderboard 🏆", output, false);
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "leaderboard";
	}

	@Override
	public String getDescription() {
		return "Look at the top 10 richest users";
	}

	@Override
	public boolean isPublic() {
		return true;
	}
}
