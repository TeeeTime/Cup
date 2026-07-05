package de.teaz.nexus.discord;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface Command {
	
	public void execute(MessageReceivedEvent event);
	
	public String getUsage();
	
	public String getDescription();
	
	public boolean isPublic();

}
