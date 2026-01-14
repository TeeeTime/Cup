package cup.discordbot.commands;

import java.util.function.Consumer;

import cup.database.LiteSQL;
import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.requests.RestAction;

public class ShutdownCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		if(event.getAuthor().getId().equals(DiscordBot.INSTANCE.getAdminId())) {
			LiteSQL.disconnect();
			RestAction<Message> action = event.getChannel().sendMessage("`Shuting down`");
			Consumer<Message> callback = (message) ->  {
				System.out.println("Shuting down...");
		        event.getJDA().shutdown();
		        System.exit(0);
            }; 
		      
            action.queue(callback);
		}
		
	}

	@Override
	public String getUsage() {
		return "$shutdown";
	}

	@Override
	public String getDescription() {
		return "Bye bye";
	}

	@Override
	public boolean isPublic() {
		return false;
	}

}
