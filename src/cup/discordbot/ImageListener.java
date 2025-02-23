package cup.discordbot;

import java.util.Random;

import cup.util.ChatGPT;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ImageListener extends ListenerAdapter {
	
	public void onMessageReceived(MessageReceivedEvent event) {
		
		Message message = event.getMessage();
		if(message.getAttachments().size() < 1) return;
		
		if(message.getAttachments().get(0).isImage()) {
			
			Random random = new Random();
			int randomInt = random.nextInt(5);
			
			if(randomInt == 0) {
				ChatGPT chatGPT = new ChatGPT(DiscordBot.INSTANCE.getChatGPTToken());
				message.reply(chatGPT.getImageResponse("You are a user in a discord taking part in a conversation. Comment on this image in a fun way. Don't describe it. You may give more background informations if posible. Translate text thats not in german or english! Keep it short (4 sentences max)", event.getMessage().getAttachments().get(0).getUrl())).queue();
			}
		}
	}
}
