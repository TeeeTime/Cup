package cup.discordbot.commands;

import java.util.Map.Entry;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class HelpCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length == 1) {
			String commandList = "";
			
			for(Entry<String, Command> entry : DiscordBot.INSTANCE.getCommandManager().getCommands().entrySet()) {
				if(entry.getValue().isPublic()) commandList += "**" + entry.getKey() + "** `" + entry.getValue().getUsage() + "`\n";
			}
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(DiscordBot.EMBEDCOLOR);
			eb.setTitle("Help");
			eb.setDescription("Here is a list of all commands");
			eb.addField("Commands:", commandList, false);
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
		}else if(args.length == 2) {
			if(DiscordBot.INSTANCE.getCommandManager().getCommands().containsKey(args[1])) {
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(DiscordBot.EMBEDCOLOR);
				eb.setTitle(args[1]);
				eb.setDescription(DiscordBot.INSTANCE.getCommandManager().getCommands().get(args[1]).getDescription());
				eb.addField("Usage:", "`" + DiscordBot.INSTANCE.getCommandManager().getCommands().get(args[1]).getUsage() + "`", false);
				
				event.getChannel().sendMessageEmbeds(eb.build()).queue();
			}else {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.commandDoesntExistEmbed().build()).queue();
				return;
			}
			
		}else {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "help [command]";
	}

	@Override
	public String getDescription() {
		return "Get help";
	}

	@Override
	public boolean isPublic() {
		return true;
	}

}
