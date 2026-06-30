package cup.discordbot.commands;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class PingCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length != 1) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		DiscordBot.INSTANCE.getJDA().getRestPing().queue( (time) ->
			event.getChannel().sendMessageFormat(":signal_strength: `Ping: %d ms`", time).queue()
		);

	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() +  "ping";
	}

	@Override
	public String getDescription() {
		return "Shows you the ping between discord servers and the bot";
	}

	@Override
	public boolean isPublic() {
		return false;
	}

}
