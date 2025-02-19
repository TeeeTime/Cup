package cup.discordbot;

import cup.util.ChatGPT;
import cup.util.CounterstrikeBlog;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CounterstrikeChangelogListener extends ListenerAdapter{
	
	public void onMessageReceived(MessageReceivedEvent event) {
		if(!event.getChannel().getName().equals("update")) return;
		
		if(event.getMessage().getContentRaw().contains("Counter-Strike 2 Update")) {
			String rawMessage = event.getMessage().getContentRaw();
			String link = rawMessage.substring(rawMessage.indexOf("("), rawMessage.indexOf(")"));
			
			CounterstrikeBlog blog = new CounterstrikeBlog();
			String changelog = blog.extractChangelog(link);
			TextChannel textChannel = event.getGuild().getTextChannelsByName("counter-strike",true).get(0);
			
			textChannel.sendMessage("New update :tada: :tada: :tada:\n\n" + changelog).queue();
			
			ChatGPT chatGPT = new ChatGPT(DiscordBot.INSTANCE.getChatGPTToken());
			
			textChannel.sendMessage("\n" + chatGPT.getResponse("You are part of a friend group that plays CS2. Write a discord message. Dont add Hashtags to the message! Make a funny and very sarcastic comment on multiple aspects of this new changelog: \"" + changelog + "\". Make it seam as if you are a real person. You may add discord emojis to make your message appear more human.")).queue();
		}
		
		
		
		
	}

}
