package cup.discordbot;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import cup.ai.ChatGPT;
import cup.ai.Response;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

public class AIListener extends ListenerAdapter {
	
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(!event.getMessage().getContentRaw().toLowerCase().startsWith("teaz")) return;
		if(event.getAuthor().isBot()) return;
		
		event.getChannel().sendTyping().queue();
		
		ChatGPT chatGPT = new ChatGPT(DiscordBot.INSTANCE.getChatGPTToken());
		
		String imageURL = "NONE";
		
		if(event.getMessage().getAttachments().size() > 0) {
			if(event.getMessage().getAttachments().get(0).isImage()) {
				imageURL = event.getMessage().getAttachments().get(0).getUrl();
			}
		}
		
		Response response = chatGPT.getAssistantResponse(event.getMessage().getContentRaw().replace("teaz", "").trim(), imageURL);
		
		if(response.getImageURLs().size() == 0) {
			event.getMessage().reply(response.getText()).queue();
		}else {
			try {
				InputStream stream = new URL(response.getImageURLs().get(0)).openStream();
				FileUpload upload = FileUpload.fromData(stream, "img.jpg");

			    event.getMessage().reply(response.getText())
			         .addFiles(upload)
			         .queue();
			    
			    return;
			    
			} catch (IOException e) {
			    event.getMessage().reply("Something went wrong :(").queue();
			    e.printStackTrace();
			}
		}
		
		
		
	}
}
