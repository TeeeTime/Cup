package cup.discordbot.commands;

import java.time.Instant;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PayCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentRaw().substring(1).trim().split(" ");
		
		if(args.length != 3 ) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		if(event.getMessage().getMentions().getMembers().size() != 1) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		
		Member member = event.getMessage().getMentions().getMembers().get(0);
		
		int amount;
		
		try {
			amount = Integer.parseInt(args[2]);
		} catch(NumberFormatException e) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		if(amount < 1) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.minimumAmountEmbed(1).build()).queue();
			return;
		}
		
		if((CoinManager.getCoins(event.getAuthor()) - amount) < 0) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.insufficientBalanceEmbed(amount - CoinManager.getCoins(event.getAuthor())).build()).queue();
			return;
		}
		
		CoinManager.setCoins(event.getAuthor(), CoinManager.getCoins(event.getAuthor()) - amount);
		CoinManager.setCoins(member.getUser(), CoinManager.getCoins(member.getUser()) + amount);
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(DiscordBot.EMBEDCOLOR);
		eb.setTitle("Transaction");
		
		eb.setDescription("```diff\n-" + amount + " " + event.getAuthor().getName() + "\n+" + amount + " " + member.getUser().getName() + "```");
		eb.setTimestamp(Instant.now());
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
		
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "pay <user> <amount>";
	}

	@Override
	public String getDescription() {
		return "Donate your hard earned coins to others";
	}

	@Override
	public boolean isPublic() {
		return true;
	}

}
