package cup.discordbot.commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import cup.database.LiteSQL;
import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class WalletCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length != 1) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		ResultSet results = LiteSQL.onQuery("SELECT count(DISTINCT balance) AS place FROM coins WHERE (balance > " + CoinManager.getCoins(event.getAuthor()) + ")");
		
		int place = 1;
		
		try {
			if(results.next()) {
				place += results.getInt("place");
			}
		} catch(SQLException e) {
			System.out.println("[LiteSQL] An error occurred while retrieving users position in leaderboard for wallet overview");
			e.printStackTrace();
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.ORANGE);
		eb.setTitle(event.getAuthor().getName() + "'s wallet :moneybag:");
		eb.setThumbnail(event.getAuthor().getAvatarUrl());
		eb.addField("Balance:", CoinManager.getCoins(event.getAuthor()) + " :coin:", true);
		eb.addField("Place:", place + "", true);
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "wallet";
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
