package cup.discordbot.commands;

import java.awt.Color;
import java.util.List;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import cup.games.Blackjack;
import cup.games.Card;
import cup.games.GameState;
import cup.games.Stats;
import cup.util.CustomEmoji;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class BlackjackCommand extends ListenerAdapter implements Command {

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length > 2) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		if(args.length == 1) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.ORANGE);
			eb.setTitle("Blackjack");
			eb.setDescription("This is the classic blackjack game we all know and love.\n"
					+ "[Wikipedia](https://en.wikipedia.org/wiki/Blackjack)"
					+ "\n\nTo start, type `" + DiscordBot.INSTANCE.getPrefix() + "blackjack [bet]`");
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			
			return;
		}
		
		int bet;
		
		try {
			bet = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		if(DiscordBot.INSTANCE.getBlackjackManager().contains(event.getAuthor().getId())) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.multipleBlackjacksEmbed().build()).queue();
			return;
		}
		
		if(bet < 20) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.minimumBetEmbed(20).build()).queue();
			return;
		}
		
		if(bet > CoinManager.getCoins(event.getAuthor())) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.insufficientBalanceEmbed(bet - CoinManager.getCoins(event.getAuthor())).build()).queue();
			return;
		}
		
		CoinManager.setCoins(event.getAuthor(), CoinManager.getCoins(event.getAuthor()) - bet);
		
		Blackjack blackjack = new Blackjack(bet);
		DiscordBot.INSTANCE.getBlackjackManager().addBlackjack(event.getAuthor().getId(), blackjack);
		
		
		if(blackjack.getGameState() == GameState.INSTANTBLACKJACK) {
			CoinManager.setCoins(event.getAuthor(), CoinManager.getCoins(event.getAuthor()) + (blackjack.getBet() * 2) + (blackjack.getBet() / 2));
			DiscordBot.INSTANCE.getBlackjackManager().removeBlackjack(event.getAuthor().getId());
			Stats.incrementStat("blackjackPlayed", event.getAuthor());
			Stats.incrementStat("blackjackWon", event.getAuthor());
			
		}else if(blackjack.getGameState() == GameState.TIE) {
			CoinManager.setCoins(event.getAuthor(), CoinManager.getCoins(event.getAuthor()) + blackjack.getBet());
			DiscordBot.INSTANCE.getBlackjackManager().removeBlackjack(event.getAuthor().getId());
			Stats.incrementStat("blackjackPlayed", event.getAuthor());
			Stats.incrementStat("blackjackTied", event.getAuthor());
			
		}else if(blackjack.getGameState() == GameState.DEALERWIN) {
			DiscordBot.INSTANCE.getBlackjackManager().removeBlackjack(event.getAuthor().getId());
			Stats.incrementStat("blackjackPlayed", event.getAuthor());
			Stats.incrementStat("blackjackLost", event.getAuthor());
			
		}
		
		if(blackjack.isPlaying()) {
			event.getChannel().sendMessageEmbeds(getGameEmbed(blackjack, event.getAuthor()).build()).addActionRow(
					Button.secondary("bj-h-" + event.getAuthor().getId(), "HIT"),
					Button.secondary("bj-s-" + event.getAuthor().getId(), "STAND")
				    ).complete();
		}else {
			event.getChannel().sendMessageEmbeds(getGameEmbed(blackjack, event.getAuthor()).build()).queue();
		}

	}

	public void onButtonInteraction(ButtonInteractionEvent event) {
		if(!event.getComponentId().startsWith("bj")) {
			return;
		}
		
		String[] args = event.getComponentId().split("-");
		
		if(!event.getUser().getId().equals(args[2])) {
			return;
		}
		
		event.deferEdit().queue();
		event.getMessage().editMessageComponents().complete();

		if(!DiscordBot.INSTANCE.getBlackjackManager().contains(args[2])) {
			return;
		}
		
		Blackjack game = DiscordBot.INSTANCE.getBlackjackManager().getBlackjack(args[2]);
		
		switch(args[1]) {
		case "s": game.stand(); break;
		case "h": game.hit(); break;
		}
		
		if(game.getGameState() == GameState.DEALERWIN) {
			DiscordBot.INSTANCE.getBlackjackManager().removeBlackjack(event.getUser().getId());
			System.out.println("l");
			Stats.incrementStat("blackjackPlayed", event.getUser());
			Stats.incrementStat("blackjackLost", event.getUser());
			
		}else if(game.getGameState() == GameState.INSTANTBLACKJACK) {
			CoinManager.setCoins(event.getUser(), CoinManager.getCoins(event.getUser()) + (game.getBet() * 2) + (game.getBet() / 2));
			DiscordBot.INSTANCE.getBlackjackManager().removeBlackjack(event.getUser().getId());
			
		}else if(game.getGameState() == GameState.PLAYERWIN) {
			CoinManager.setCoins(event.getUser(), CoinManager.getCoins(event.getUser()) + (game.getBet() * 2));
			DiscordBot.INSTANCE.getBlackjackManager().removeBlackjack(event.getUser().getId());
			Stats.incrementStat("blackjackPlayed", event.getUser());
			Stats.incrementStat("blackjackWon", event.getUser());
			
		}else if(game.getGameState() == GameState.TIE) {
			CoinManager.setCoins(event.getUser(), CoinManager.getCoins(event.getUser()) + game.getBet());
			DiscordBot.INSTANCE.getBlackjackManager().removeBlackjack(event.getUser().getId());
			Stats.incrementStat("blackjackPlayed", event.getUser());
			Stats.incrementStat("blackjackTied", event.getUser());
		}
		
		if(game.isPlaying()) {
			event.getMessage().editMessageEmbeds(getGameEmbed(game, event.getUser()).build()).setActionRow(
					Button.secondary("bj-h-" + event.getUser().getId(), "HIT"),
					Button.secondary("bj-s-" + event.getUser().getId(), "STAND")
				    ).complete();
		}else {
			event.getMessage().editMessageEmbeds(getGameEmbed(game, event.getUser()).build()).queue();
		}
	}
	
	private EmbedBuilder getGameEmbed(Blackjack blackjack, User user) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setTitle("Blackjack");
		
		switch(blackjack.getGameState()) {
		case PLAYING: eb.setColor(Color.ORANGE); break;
		case DEALERWIN: eb.setColor(Color.RED); eb.setTitle("Lose"); break;
		case END: eb.setColor(Color.ORANGE); break;
		case INSTANTBLACKJACK: eb.setColor(Color.GREEN); eb.setTitle("Win"); break;
		case PLAYERWIN: eb.setColor(Color.GREEN); eb.setTitle("Win"); break;
		case TIE: eb.setColor(Color.GRAY); eb.setTitle("Tie"); break;
		}

		String embedText = "";
		
		List<Card> dealerCards = blackjack.getDealerCards();
		String dealerCardsText = "";
		
		for(Card card : dealerCards) {
			dealerCardsText += CustomEmoji.getFormated(card.toString());
		}
		
		embedText += "**Dealer:**\n"
				+ "# **`" + blackjack.getDealerValue() + "`**  " + dealerCardsText + "\n\n";
		
		List<Card> playerCards = blackjack.getPlayerCards();
		String playerCardsText = "";
		
		
		for(Card card : playerCards) {
			playerCardsText += CustomEmoji.getFormated(card.toString());
		}
		
		embedText += "**" + user.getName() + ":**\n"
				+ "# **`" + blackjack.getPlayerValue() + "`**  " + playerCardsText + "\n";
		
		if(blackjack.getGameState() == GameState.DEALERWIN) {
			embedText += "\nYou lost " + blackjack.getBet() + " :coin:";
			
		}else if(blackjack.getGameState() == GameState.INSTANTBLACKJACK) {
			embedText += "\nYou won " + ((blackjack.getBet() * 2) + (blackjack.getBet() / 2)) + " :coin:";
			
		}else if(blackjack.getGameState() == GameState.PLAYERWIN) {
			embedText += "\nYou won " + (blackjack.getBet() * 2) + " :coin:";
			
		}else if(blackjack.getGameState() == GameState.TIE) {
			embedText += "\nYour balance stays untouched";
		}
		
		eb.setDescription(embedText);
		
		return eb;
	}
	
	@Override
	public String getUsage() {

		return DiscordBot.INSTANCE.getPrefix() + "blackjack [bet]";
	}

	@Override
	public String getDescription() {

		return "Play a game of blackjack for a set bet.";
	}

	@Override
	public boolean isPublic() {

		return true;
	}

}
