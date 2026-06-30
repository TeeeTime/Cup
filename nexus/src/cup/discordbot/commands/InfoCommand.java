package cup.discordbot.commands;

import java.awt.Color;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class InfoCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length != 1) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GRAY);
		eb.setTitle("Info");
		
		eb.addField("Operating System:", "`" + System.getProperty("os.name") + "`", false);
		
		MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
		String memoryUsage = "";
		memoryUsage += String.format("%.2f", (double)memoryMXBean.getHeapMemoryUsage().getUsed() /1073741824);
		memoryUsage += " / ";
		memoryUsage += String.format("%.2f", (double)memoryMXBean.getHeapMemoryUsage().getMax() /1073741824);
		memoryUsage += " GB";

		eb.addField("Memory usage:", "`" + memoryUsage + "`", false);
		
		eb.addField("Available Cores:", "`" + Runtime.getRuntime().availableProcessors() + "`", false);
		
		DiscordBot.INSTANCE.getJDA().getRestPing().queue( (time) ->
			event.getChannel().sendMessageEmbeds(eb.addField("Ping to discord:", "`" + time + " ms`", false).build()).queue()
		);
	    
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "info";
	}

	@Override
	public String getDescription() {
		return "Shows informations about the server it is running on";
	}

	@Override
	public boolean isPublic() {
		return false;
	}

}
