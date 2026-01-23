package cup.discordbot;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


public class GuildJoinListener extends ListenerAdapter {	
	
	public void onGuildMemberJoin(GuildMemberJoinEvent event) {
		if(event.getUser().isBot()) return;
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(DiscordBot.EMBEDCOLOR);
		eb.setTitle(event.getUser().getName() + " just joined the discord!");
		eb.setDescription("We are now " + event.getGuild().getMembers().size() + " members");
		
		DiscordBot.INSTANCE.getJDA().getTextChannelById("1248630878159114240").sendMessageEmbeds(eb.build()).queue();
	}

}
