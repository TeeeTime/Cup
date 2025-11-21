package cup.discordbot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import cup.util.ChatGPT;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

public class AIListener extends ListenerAdapter {
	
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(!event.getMessage().getContentRaw().toLowerCase().startsWith("teaz")) return;
		if(event.getAuthor().isBot()) return;
		
		ChatGPT chatGPT = new ChatGPT(DiscordBot.INSTANCE.getChatGPTToken());
		
		String imageURL = "NONE";
		
		if(event.getMessage().getAttachments().size() > 0) {
			if(event.getMessage().getAttachments().get(0).isImage()) {
				imageURL = event.getMessage().getAttachments().get(0).getUrl();
			}
		}
		
		String reply = chatGPT.getAssistantResponse(event.getMessage().getContentRaw().replace("teaz", "").trim(), imageURL);
		
		if(reply.startsWith("https://oaidalleapiprodscus")) {
			try {
				InputStream stream = new URL(reply).openStream();
				FileUpload upload = FileUpload.fromData(stream, "img.jpg");

			    event.getMessage().reply("Here you go :)")
			         .addFiles(upload)
			         .queue();
			    
			    return;
			    
			} catch (IOException e) {
			    event.getMessage().reply("Could not load the image").queue();
			    e.printStackTrace();
			}
		}
		
		event.getMessage().reply(reply).queue();
		
	}
}
