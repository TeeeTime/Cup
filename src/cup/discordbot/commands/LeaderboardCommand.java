package cup.discordbot.commands;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cup.database.LiteSQL;
import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import cup.economy.LeaderboardEntry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
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
			output += "**`" + users.get(i).getRank() + ".`** `" + users.get(i).getName() + "` (" + users.get(i).getBalance() + " :coin:)\n";
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

	private List<Member> sortMemberList(List<Member> list){
		
		boolean change = true;
		
		while(change) {
			change = false;
			for(int i = 0; i < (list.size() - 1); i++) {
				if(CoinManager.getCoins(list.get(i).getUser().getId()) < CoinManager.getCoins(list.get(i + 1).getUser().getId())) {
					
					Member current = list.get(i);
					list.set(i, list.get(i + 1));
					list.set(i + 1, current);
					
					change = true;
				}
			}
		}
		
		return list;
		
	}
}
