package cup.discordbot.commands;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class StakeCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		
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
		return false;
	}

}
