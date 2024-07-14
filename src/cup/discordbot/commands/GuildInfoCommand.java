package cup.discordbot.commands;

import java.awt.Color;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.ForumChannel;
import net.dv8tion.jda.api.entities.channel.concrete.NewsChannel;
import net.dv8tion.jda.api.entities.channel.concrete.StageChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.RichCustomEmoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class GuildInfoCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length != 1) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		Guild guild = event.getGuild();
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.GRAY);
		eb.setTitle(guild.getName());
		
		if(guild.getIconUrl() != null) eb.setThumbnail(guild.getIconUrl());
		if(guild.getBannerUrl() != null) eb.setImage(guild.getBannerUrl());
		
		eb.addField("Id:", "`" + guild.getId() + "`", false);
		eb.addField("Owner id:", "`" + event.getGuild().getOwnerId() + "`", false);
		eb.addField("Member size:", "`" + guild.getMemberCount() + "`", false);
		eb.addField("Creation date:", "<t:" +  guild.getTimeCreated().toEpochSecond() + ":d>", false);
		
		String roles = "";
		for(Role role : guild.getRoles()) {
			roles += role.getAsMention() + " ";
		}
		
		eb.addField("Roles:", roles, false);
		
		String structure = "";
		for(Category category : guild.getCategories()) {
			structure += "╒ **" + category.getName() + "**\n";
			
			for(NewsChannel newsChannel : category.getNewsChannels()) {
				structure += "╘ :new: " + newsChannel.getName() + "\n";
			}
			
			for(ForumChannel forumChannel : category.getForumChannels()) {
				structure += "╘ :speech_left: " + forumChannel.getName() + "\n";
			}
			
			for(TextChannel textChannel : category.getTextChannels()) {
				structure += "╘ **#** " + textChannel.getName() + "\n";
			}
			
			for(StageChannel stageChannel : category.getStageChannels()) {
				structure += "╘ :mega: " + stageChannel.getName() + "\n";
			}
			
			for(VoiceChannel voiceChannel : category.getVoiceChannels()) {
				structure += "╘ :loud_sound: " + voiceChannel.getName() + "\n";
			}
			
			structure += "\n";
		}
		
		eb.addField("Structure:", structure, false);
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
		
		//eb2 serves the purpose of displaying the emojis in a different embed to avoid hitting the character limit of embeds
		EmbedBuilder eb2 = new EmbedBuilder();
		eb2.setColor(Color.GRAY);
		eb2.setTitle("Emojis:");
		
		String emojis = "";
		for(RichCustomEmoji emoji : guild.getEmojis()) {
			emojis += emoji.getFormatted() + " ";
		}
		eb2.setDescription(emojis);
		
		if(!emojis.equals("")) event.getChannel().sendMessageEmbeds(eb2.build()).queue();
		
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "guildinfo";
	}

	@Override
	public String getDescription() {
		return "A command mainly for development to print out information about the server.";
	}

	@Override
	public boolean isPublic() {
		return false;
	}

}
