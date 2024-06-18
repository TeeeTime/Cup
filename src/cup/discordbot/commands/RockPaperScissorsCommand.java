package cup.discordbot.commands;

import java.awt.Color;
import java.util.Random;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import cup.games.Stats;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class RockPaperScissorsCommand extends ListenerAdapter implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length == 1) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.ORANGE);
			eb.setTitle("Rock Paper Scissors");
			eb.setDescription("Rock Paper Scissors is a game where you choose one symbol and battle against the computer.\n\nTo start, type `$rps [bet]`");
			eb.addField(":rock:" + " Rock", "Rock beats Scissors", false);
			eb.addField(":page_facing_up:" + " Raper", "Paper beats Rock", false);
			eb.addField(":scissors:" + " Scissors", "Scissors beat Paper", false);
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			
			return;
		}
		
		if(args.length == 2) {
			int bet = 0;
			
			try {
				bet = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
				return;
			}
			
			if(bet > CoinManager.getCoins(event.getAuthor())) {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.insufficientBalanceEmbed(bet - CoinManager.getCoins(event.getAuthor())).build()).queue();
				return;
			}
			
			if(bet < 10) {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.minimumBetEmbed(10).build()).queue();
				return;
			}
			
			CoinManager.setCoins(event.getAuthor(), CoinManager.getCoins(event.getAuthor()) - bet);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.ORANGE);
			eb.setTitle("Rock Paper Scissors");
			eb.setDescription("**Bet:** " + bet + " :coin:\nChoose your symbol");
			
			event.getChannel().sendMessageEmbeds(eb.build()).addActionRow(
					Button.secondary("rps-" + event.getAuthor().getId() + "-r-" + bet, Emoji.fromUnicode("🪨")),
					Button.secondary("rps-" + event.getAuthor().getId() + "-p-" + bet, Emoji.fromUnicode("📄")),
					Button.secondary("rps-" + event.getAuthor().getId() + "-s-" + bet, Emoji.fromUnicode("✂"))
					).queue();
			
		}else {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
		}
		
	}
	
	public void onButtonInteraction(ButtonInteractionEvent event) {
        if(event.getComponentId().startsWith("rps")) {
            String[] args = event.getComponentId().split("-");
            
            
            if(event.getUser().getId().equals(args[1])) {
            	String userSign = args[2];
            	String botSign = randomSign();
            	
            	String combine = userSign + botSign;
            	String result = "t";
            	switch(combine) {
            	case "rr": result = "t"; break;
            	case "rp": result = "l"; break;
            	case "rs": result = "w"; break;
            	case "pr": result = "w"; break;
            	case "pp": result = "t"; break;
            	case "ps": result = "l"; break;
            	case "sr": result = "l"; break;
            	case "sp": result = "w"; break;
            	case "ss": result = "t"; break;
            	}
            	
            	String emojiRepresentation = representationToEmoji(userSign) + " vs " + representationToEmoji(botSign);
            	
            	
            	EmbedBuilder eb = new EmbedBuilder();
            	int bet = 0;
            	
            	try {
    				bet = Integer.parseInt(args[3]);
    			} catch(NumberFormatException e) {
    				e.printStackTrace();
    			}
            	
            	switch(result) {
            	case "w": eb.setColor(Color.GREEN); eb.addField("Winner", emojiRepresentation + "\n\nYou won " + bet + " :coin:", false); CoinManager.setCoins(event.getUser(), CoinManager.getCoins(event.getUser()) + (bet * 2)); Stats.incrementStat("rpsWon", event.getUser()); Stats.incrementStat("rpsPlayed", event.getUser()); break;
            	case "t": eb.setColor(Color.GRAY); eb.addField("Tie", emojiRepresentation + "\n\nYour balance stays untouched", false); CoinManager.setCoins(event.getUser(), CoinManager.getCoins(event.getUser()) + bet); Stats.incrementStat("rpsTied", event.getUser()); Stats.incrementStat("rpsPlayed", event.getUser()); break;
            	case "l": eb.setColor(Color.RED); eb.addField("Loser", emojiRepresentation + "\n\nYou lost " + bet + " :coin:", false); Stats.incrementStat("rpsLost", event.getUser()); Stats.incrementStat("rpsPlayed", event.getUser()); break;
            	}
            	
            	event.editMessageEmbeds(eb.build()).setComponents().complete();
            }
        }
	}
	
	private String randomSign() {
		Random r = new Random();
		
		switch(r.nextInt(3)) {
		case 0: return "r";
		case 1: return "p";
		case 2: return "s";
		}
		return "r";
	}
	
	private String representationToEmoji(String sign) {
		switch(sign) {
    	case "r": return ":rock:"; 
    	case "p": return ":page_facing_up:";
    	case "s": return ":scissors:";
    	}
		return "error";
	}
	
	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "rps [bet]";
	}

	@Override
	public String getDescription() {
		return "Rock Paper Scissors game. Set your stakes and gamble";
	}

	@Override
	public boolean isPublic() {
		return true;
	}
}
