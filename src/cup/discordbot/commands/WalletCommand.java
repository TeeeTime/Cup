package cup.discordbot.commands;

import java.sql.ResultSet;

import cup.database.LiteSQL;
import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WalletCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().substring(1).trim().split(" ");
		
		if(args.length > 2) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		User user;
		if(args.length == 1) {
			 user = event.getAuthor();
			
		}else {
			if(event.getMessage().getMentions().getMembers().size() != 1) {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
				return;
			}
			
			user = event.getMessage().getMentions().getMembers().get(0).getUser();
		}
		int place = 1;
		
		try {
			ResultSet results = LiteSQL.onQuery("SELECT count(DISTINCT balance) AS place FROM coins WHERE (balance > " + CoinManager.getCoins(user.getId()) + ")");
		
			if(results.next()) {
				place += results.getInt("place");
			}
		} catch(Exception e) {
			System.out.println("[LiteSQL] An error occurred while retrieving users position in leaderboard for wallet overview");
			e.printStackTrace();
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(DiscordBot.EMBEDCOLOR);
		eb.setTitle(user.getName() + "'s wallet :moneybag:");
		eb.setThumbnail(user.getAvatarUrl());
		eb.addField("Balance:", CoinManager.getCoins(user.getId()) + " :coin:", true);
		eb.addField("Place:", place + "", true);
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
		
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "wallet <user>";
	}

	@Override
	public String getDescription() {
		return "Look into your wallet and see how wealthy you are";
	}

	@Override
	public boolean isPublic() {
		return true;
	}

}
