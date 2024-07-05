package cup.discordbot.commands;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EmojisCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		
		String emojiList = "";
		
		for(RichCustomEmoji emoji : event.getGuild().getEmojis()) {
			emojiList += emoji.getName() + ": " + emoji.getFormatted() + "\n";
		}
		
		event.getChannel().sendMessage(emojiList).queue();
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "emojis";
	}

	@Override
	public String getDescription() {
		return "Returns a list with useful informations about the custom emojis of the guild.";
	}

	@Override
	public boolean isPublic() {
		return false;
	}

}
