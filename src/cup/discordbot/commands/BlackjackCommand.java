package cup.discordbot.commands;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class BlackjackCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		//TO DO
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

		return false;
	}

}
