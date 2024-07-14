package cup.discordbot.commands;

import java.awt.Color;
import java.time.Instant;

import cup.discordbot.Command;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class RulesCommand extends ListenerAdapter implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		if(event.getAuthor().getId().equals("359013020057206786")) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.ORANGE);
			eb.setTitle("Rules");
			eb.setDescription("You need to accept the rules in order to get access to the discord");
			eb.addField("1", "Be nice and respectful to others", false);
			eb.addField("2", "No spamming", false);
			eb.addField("3", "No self promotion", false);
			eb.addField("4", "Discord TOS", false);
			eb.setTimestamp(Instant.now());
			
			event.getChannel().sendMessageEmbeds(eb.build()).addActionRow(
					Button.success("rulesaccept", Emoji.fromUnicode("✅"))
					).queue();
			
			event.getMessage().delete().queue();
		}
	}
	
	public void onButtonInteraction(ButtonInteractionEvent event) {
		if(event.getComponentId().equals("rulesaccept")) {
			event.getGuild().addRoleToMember(event.getUser(), event.getJDA().getRoleById("678647130935656500")).queue();
			event.deferEdit().queue();
		}
	}

	@Override
	public String getUsage() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public boolean isPublic() {
		return false;
	}
	
	

}
