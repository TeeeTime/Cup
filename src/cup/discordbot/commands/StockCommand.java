package cup.discordbot.commands;

import java.awt.Color;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.util.Stock;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StockCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length != 2) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		Stock stock = DiscordBot.INSTANCE.getStocksAPI().getStock(args[1]);
		
		if(stock == null) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.cantFindStockEmbed(args[1]).build()).queue();
			return;
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.ORANGE);
		eb.setTitle(stock.getName() + " :chart_with_upwards_trend:");
		eb.addField("Ticker:", "`" + stock.getTicker() + "`", false);
		eb.addField("Volume:", "`" + stock.getVolume() + "`", false);
		eb.addField("Price:", "`" + stock.getPrice() + " USD`", false);
		eb.addField("Highest today:", "`" + stock.getDayHigh() + " USD`", false);
		eb.addField("Lowest today:", "`" + stock.getDayLow() + " USD`", false);
		eb.addField("Highest in 52 weeks:", "`" + stock.getYearHigh() + " USD`", false);
		eb.addField("Lowest in 52 weeks:", "`" + stock.getYearLow() + " USD`", false);
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "stock [ticker]";
	}

	@Override
	public String getDescription() {
		return "Retrieve on stocks from stockdata.org API";
	}

	@Override
	public boolean isPublic() {
		return false;
	}

}
