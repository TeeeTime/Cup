package cup.discordbot;

import cup.util.ChatGPT;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class AIListener extends ListenerAdapter {
	
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(!event.getMessage().getContentRaw().toLowerCase().startsWith("teaz")) return;
		if(event.getAuthor().isBot()) return;
		
		ChatGPT chatGPT = new ChatGPT(DiscordBot.INSTANCE.getChatGPTToken());
		
		String reply = "";
		
		if(event.getMessage().getAttachments().size() > 0) {
			if(event.getMessage().getAttachments().get(0).isImage()) {
				System.out.println(event.getMessage().getAttachments().get(0).getUrl());
				
				reply = chatGPT.getImageResponse("You are an assistant ai in a discord server. Your name is \"TEAZ\". You reply in a casual and funny way. You should provide valuable information. If there is text on the image thats not in german or english, translate it! When asked for a location, provide a guess with the data provided! Always reply in english. You may use emojis with discord formating. Message: \"" + event.getMessage().getContentRaw().replace("teaz", "") + "\"", 
						event.getMessage().getAttachments().get(0).getUrl());
			}
		}else {
			reply = chatGPT.getResponse("You are an assistant ai in a discord server. Your name is \"TEAZ\". You reply in a casual and funny way. You should provide valuable information. Always reply in english. You may use emojis with discord formating.", 
					event.getMessage().getContentRaw().replace("teaz ", ""));
		}
		
		
		event.getMessage().reply(reply).queue();
		
	}
}
