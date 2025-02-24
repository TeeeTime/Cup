package cup.discordbot;

import cup.util.ChatGPT;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AIListener extends ListenerAdapter {
	
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(!event.getMessage().getContentRaw().split("")[0].toLowerCase().equals("teaz")) return;
		if(event.getAuthor().isBot()) return;
		
		ChatGPT chatGPT = new ChatGPT(DiscordBot.INSTANCE.getChatGPTToken());
		String reply = chatGPT.getResponse("You are an assistant ai in a discord server. You reply in a casual and funny way. Always reply in english. You may use emojis with discord formating. If you are asked to start a race, put {race} at the beginning of the reply. Be sure to use this exact formating.", 
				event.getMessage().getContentRaw().replace("teaz ", ""));
		
		if(reply.startsWith("{race}")) {
			reply = reply.replace("{race}", "`Introducing Teaz Actions. Teaz will soon be able to control certain functions.`\n");
		}
		
		event.getMessage().reply(reply).queue();
		
	}
}
