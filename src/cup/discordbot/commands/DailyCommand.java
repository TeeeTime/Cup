package cup.discordbot.commands;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import cup.database.LiteSQL;
import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class DailyCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length != 1) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		
		LocalDate currentDate = LocalDate.now();
		
		String currentDateFormated = currentDate.format(format);
		
		User user = event.getAuthor();
		
		if(!entryExists(event.getAuthor())) {
			CoinManager.setCoins(user, CoinManager.getCoins(user) + 500);
			
			putNewDate(user, currentDateFormated);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(DiscordBot.EMBEDCOLOR);
			eb.addField("Daily bonus claimed!", "You received 500 :coin:", false);
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			
			return;
		}
		
		if(!currentDateFormated.equals(getDate(user))) {
			CoinManager.setCoins(user, CoinManager.getCoins(user) + 500);
			
			putNewDate(user, currentDateFormated);
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(DiscordBot.EMBEDCOLOR);
			eb.addField("Daily bonus claimed!", "You received 500 :coin:", false);
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			
		}else {
			LocalDateTime currentDateTime = LocalDateTime.now();

			String hours, minutes = "";
			
			if(currentDateTime.getMinute() == 0) {
				hours = 24 - currentDateTime.getHour() + "h";
				minutes = "";
				
			}else {
				hours = 23 - currentDateTime.getHour() + "h";
				minutes = 60 - currentDateTime.getMinute() + "m";
			}
			
			String cooldown = hours + " " + minutes;
			
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.dailyClaimedEmbed(this, cooldown).build()).queue();
		}
		
	}
	
	private boolean entryExists(User user) {
		
		try {
			ResultSet results = LiteSQL.onQuery("SELECT lastclaimdate FROM daily WHERE userid = " + user.getId());
			if(results.next()) {
				return true;
			}else {
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private void putNewDate(User user, String date) {
		try {
			if(!entryExists(user)) {
				LiteSQL.onUpdate("INSERT INTO daily(userid, lastclaimdate) VALUES(" + user.getId() + ", '" + date + "')");
			}else {
				LiteSQL.onUpdate("UPDATE daily SET lastclaimdate = '" + date + "' WHERE userid = " + user.getId());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getDate(User user) {

		try {
			ResultSet results = LiteSQL.onQuery("SELECT lastclaimdate FROM daily WHERE userid = " + user.getId());
			
			if(results.next()) {
				return results.getString("lastclaimdate");
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "daily";
	}

	@Override
	public String getDescription() {
		return "Claim 100 :coin: daily. Resets at 12 PM GMT+2";
	}

	@Override
	public boolean isPublic() {
		return true;
	}

}
