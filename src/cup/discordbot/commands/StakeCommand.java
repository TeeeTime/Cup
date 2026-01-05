package cup.discordbot.commands;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Random;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StakeCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length > 3 || args.length == 2) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		if(args.length == 1) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(DiscordBot.EMBEDCOLOR);
			eb.setTitle("Stake");
			eb.setDescription("Stake is a game, where you set a muliplier for your bet. If you win, you get the amount you bet multiplied by the multiplier you set. The catch is, that your chances of winning get lower according to your multiplier. The chances of winning comply with the rules of a fair randomized game.\n"
					+ "\nTo start, type `" + DiscordBot.INSTANCE.getPrefix() + "stake [multiplier] [bet]`");
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			
			return;
		}
		
		int multiplier;
		
		try {
			multiplier = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		if(multiplier < 2) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.minimumMultiplierEmbed(2).build()).queue();
			return;
		}
		
		int bet;
		
		try {
			bet = Integer.parseInt(args[2]);
		} catch(NumberFormatException e) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		if(bet < 10) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.minimumBetEmbed(10).build()).queue();
			return;
		}
		
		if(bet > CoinManager.getCoins(event.getAuthor())) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.insufficientBalanceEmbed(bet - CoinManager.getCoins(event.getAuthor())).build()).queue();
			return;
		}
		
		Random random = new Random();
		int randomInt = random.nextInt(multiplier);
		float winChance = (1 / (float) multiplier) * 100;
		
		DecimalFormat df = new DecimalFormat("0.00");
		
		if(randomInt == 0) {
			CoinManager.setCoins(event.getAuthor(), CoinManager.getCoins(event.getAuthor()) + (multiplier * bet) - bet);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.GREEN);
			eb.addField("Winner", "You hit a `" + df.format(winChance) + "%` chance and won " + (multiplier * bet) + " :coin:", false);

			event.getChannel().sendMessageEmbeds(eb.build()).queue();
		}else {
			CoinManager.setCoins(event.getAuthor(), CoinManager.getCoins(event.getAuthor()) - bet);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.RED);
			eb.addField("Loser", "You missed a `" + df.format(winChance) + "%` chance and lost " + bet + " :coin:", false);

			event.getChannel().sendMessageEmbeds(eb.build()).queue();
		}
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "stake [multiplier] [bet]";
	}

	@Override
	public String getDescription() {
		return "gamble your money for a set multiplier. The higher your multiplier is, the lower are you chances of winning.";
	}

	@Override
	public boolean isPublic() {
		return true;
	}

}
