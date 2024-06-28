package cup.discordbot;

import java.awt.Color;

import net.dv8tion.jda.api.EmbedBuilder;

public class ErrorEmbedBuilder {
	
	public static EmbedBuilder usageEmbed(Command command) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("Usage:","`" + command.getUsage() + "`", true);
		
		return eb;
	}
	
	public static EmbedBuilder dailyClaimedEmbed(Command command, String cooldown) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("You already claimed your daily bonus!","You can claim your next bonus in " + cooldown, true);
		
		return eb;
	}
	
	public static EmbedBuilder commandDoesntExistEmbed() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("This command doesn't exist","Use `$help` for a list of all commands", false);
		
		return eb;
	}
	
	public static EmbedBuilder insufficientBalanceEmbed(int amount) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("Insufficient Balance", "You are missing " + amount + ":coin:", false);
		
		return eb;
	}
	
	public static EmbedBuilder minimumBetEmbed(int amount) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("Minimum bet for this game:", amount + " :coin:", false);
		
		return eb;
	}

	public static EmbedBuilder multipleRacesEmbed() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("You already have started a race", "Let the race conclude to start a new one!", false);
		
		return eb;
	}
	
	public static EmbedBuilder minimumAmountEmbed(int amount) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("Minimum amount:", amount + " :coin:", false);
		
		return eb;
	}
	
	public static EmbedBuilder minimumStockAmountEmbed(int amount) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("Minimum amount:", amount + "", false);
		
		return eb;
	}
	
	public static EmbedBuilder stockDoesntExistEmbed() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("This stock doesn't exist", "To get an overview of the available stocks use `" + DiscordBot.INSTANCE.getPrefix() + "stocks`", false);
		
		return eb;
	}
	
	public static EmbedBuilder notEnoughStocksEmbed() {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("You don't own enough stocks", "To buy more stocks, use `" + DiscordBot.INSTANCE.getPrefix() + "stock`", false);
		
		return eb;
	}
	
	public static EmbedBuilder minimumMultiplierEmbed(int amount) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.RED);
		eb.setTitle("🚫");
		eb.addField("Minimum multiplier:", "`" + amount + "x`", false);
		
		return eb;
	}
}
