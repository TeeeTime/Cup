package cup.discordbot.commands;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.economy.CoinManager;
import cup.games.Race;
import cup.games.Stats;
import cup.util.ChatGPT;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class RaceCommand extends ListenerAdapter implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length > 2) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		if(args.length == 1) {
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(Color.ORANGE);
			eb.setTitle("Race");
			eb.setDescription("Race is a game where you set a bet and a lobby is opened for others to enter. Others users get **30 seconds** to enter. Once the lobby phase is concluded, the race starts. Every player is assigned a colored dot. The dots move forward in random intervals until one reaches the goal. The winning player receives all bets collected by the other players.\n\nTo start, type `$race [bet]`");
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			
			return;
		}
		
		int bet;
		
		try {
			bet = Integer.parseInt(args[1]);
		} catch(NumberFormatException e) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
		
		if(DiscordBot.INSTANCE.getRaceManager().contains(event.getAuthor().getId())) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.multipleRacesEmbed().build()).queue();
			return;
		}
		
		if(bet < 50) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.minimumBetEmbed(50).build()).queue();
			return;
		}
		
		if(bet > CoinManager.getCoins(event.getAuthor())) {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.insufficientBalanceEmbed(bet - CoinManager.getCoins(event.getAuthor())).build()).queue();
			return;
		}
		
		CoinManager.setCoins(event.getAuthor(), CoinManager.getCoins(event.getAuthor()) - bet);
		
		Race race = new Race(bet);
		race.addUser(event.getAuthor());
		
		Stats.incrementStat("racesPlayed", event.getAuthor());
		
		DiscordBot.INSTANCE.getRaceManager().addRace(event.getAuthor().getId(), race);
		
		EmbedBuilder eb = getRaceEmbed(race);
		eb.setFooter("You have " + race.getSecondsToJoin() + " seconds to enter the race until it starts! (" + race.size() + "/8)");
		
		Message message = event.getChannel().sendMessageEmbeds(eb.build()).addActionRow(
				Button.success("race-" + event.getAuthor().getId(), "ENTER")
				).complete();
		
		Timer timer = new Timer();
		
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				
				race.decreaseSecondsToJoin(1);
				
				EmbedBuilder eb = getRaceEmbed(race);
				eb.setFooter("You have " + race.getSecondsToJoin() + " seconds to enter the race until it starts! (" + race.size() + "/8)");
				
				message.editMessageEmbeds(eb.build()).complete();
				
				if(race.getSecondsToJoin() <= 0 || race.size() == 8) {
					timer.cancel();
					
					message.editMessageEmbeds(eb.build()).setComponents().complete();
					
					if(race.size() == 1) {
						race.addUser(DiscordBot.INSTANCE.getJDA().getSelfUser());
					}
					
					EmbedBuilder raceEmbed = getRaceEmbed(race);
					raceEmbed.addField("Race:", raceAsText(race), false);
					
					message.editMessageEmbeds(raceEmbed.build()).complete();
					
					Timer raceTimer = new Timer();
					
					raceTimer.scheduleAtFixedRate(new TimerTask() {

						@Override
						public void run() {
							User movedUser = race.randomMove();
							
							EmbedBuilder raceEmbed = getRaceEmbed(race);
							raceEmbed.addField("Race:", raceAsText(race), false);
							
							
							
							if(race.getPosition(movedUser) == 19) {
								Stats.incrementStat("racesWon", movedUser);
								
								int winnings = race.getBet() * race.size();
								if(!movedUser.isBot()) {
									CoinManager.setCoins(movedUser, CoinManager.getCoins(movedUser) + winnings);
								}
								raceEmbed.addField("Winner: " + movedUser.getName() + " :trophy:", "They received " + winnings + " :coin:", false);
								
								ChatGPT chatGPT = new ChatGPT(DiscordBot.INSTANCE.getChatGPTToken());
								
								raceEmbed.setFooter(chatGPT.getResponse("Comment on this race. The race state is given by this list of usernames and a number associated with them. The user with the highest number won, the higher the number, the closer the people where to the finishline. Include every user in your comment and maybe use their usernames in wordplays or other funny sentences. Don't mention the scores, they just indicate the position of the users on the board. Keep the reply short (maximum of 3 sentences). Here is the race state: " + race.getAsFormatedText()));
								
								DiscordBot.INSTANCE.getRaceManager().removeRace(race.getUsers().get(0).getId());
								
								raceTimer.cancel();
							}
							
							message.editMessageEmbeds(raceEmbed.build()).complete();
						}
					
					}, 1000, 1000);
					
				}
				
			}
			
		}, 1000, 1000);
		
		
	}

	public void onButtonInteraction(ButtonInteractionEvent event) {
		if(event.getComponentId().startsWith("race")) {
			event.deferEdit().queue();
			String[] args = event.getComponentId().split("-");
			Race race = DiscordBot.INSTANCE.getRaceManager().getRace(args[1]);
			if(CoinManager.getCoins(event.getUser()) >= race.getBet()) {
				if(race.containsUser(event.getUser())) return;
				
				if(race.size() < 8) {
					CoinManager.setCoins(event.getUser(), CoinManager.getCoins(event.getUser()) - race.getBet());
					
					race.addUser(event.getUser());
					Stats.incrementStat("racesPlayed", event.getUser());
				}
			}
		}
	}
	
	private String participantsAsText(Race race) {
		String output = "";
		ArrayList<User> users = race.getUsers();
		
		for(int i = 0; i < users.size(); i++) {
			output += race.getEmoji(i) + " " + users.get(i).getName() + "\n";
		}
		
		return output;
	}
	
	private String raceAsText(Race race) {
		String output = "";
		
		ArrayList<User> users = race.getUsers();
		
		for(int i = 0; i < users.size(); i++) {
			
			int position = race.getPosition(users.get(i));
			String emoji = race.getEmoji(i);
			
			for(int j = 0; j < 20; j++) {
				if(j == position) {
					output += emoji;
				}else {
					output += "-";
				}
			}
			
			if(race.getPosition(users.get(i)) == 19) {
				output += ":trophy:\n";
			}else {
				output += ":checkered_flag:\n";
			}
		}
		
		return output;
	}
	
	private EmbedBuilder getRaceEmbed(Race race) {
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.YELLOW);
		eb.setTitle("Race🏁");
		eb.setDescription("**Bet:** " + race.getBet() + " :coin:");
		eb.addField("Participants:", participantsAsText(race), false);
		
		return eb;
	}
	
	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "race [bet]";
	}

	@Override
	public String getDescription() {
		return "Race against your friends or the bot and gamble your coins";
	}

	@Override
	public boolean isPublic() {
		return true;
	}

}
