package cup.discordbot;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
	
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()) {
			return;
		}
		
		if(event.getMessage().getContentDisplay().startsWith(DiscordBot.INSTANCE.getPrefix())) {
			String[] args = event.getMessage().getContentDisplay().substring(DiscordBot.INSTANCE.getPrefix().length()).trim().split(" ");
			
			if(args.length > 0) {
				DiscordBot.INSTANCE.getCommandManager().perform(args[0], event);
			}
		}
		
	}

}
