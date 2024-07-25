package cup.discordbot.commands;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SQLCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		if(!event.getAuthor().getId().equals("359013020057206786")) return;
		
		
		
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "sql <execute, query> <statement>";
	}

	@Override
	public String getDescription() {
		return "Access to the database via discord";
	}

	@Override
	public boolean isPublic() {
		return false;
	}

}
