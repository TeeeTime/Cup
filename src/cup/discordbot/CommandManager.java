package cup.discordbot;

import java.util.HashMap;

import cup.discordbot.commands.*;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class CommandManager {

private HashMap<String, Command> commands;
	
	public CommandManager() {
		this.commands = new HashMap<>();
		
		commands.put("ping", new PingCommand());
		commands.put("daily", new DailyCommand());
		commands.put("rps", new RockPaperScissorsCommand());
		commands.put("shutdown", new ShutdownCommand());
		commands.put("wallet", new WalletCommand());
		commands.put("help", new HelpCommand());
		commands.put("rules", new RulesCommand());
		commands.put("race", new RaceCommand());
		commands.put("pay", new PayCommand());
		commands.put("leaderboard", new LeaderboardCommand());
		commands.put("stats", new StatsCommand());
		commands.put("profile", new ProfileCommand());
		commands.put("guildinfo", new GuildInfoCommand());
		commands.put("blackjack", new BlackjackCommand());
		commands.put("emojis", new EmojisCommand());
		commands.put("info", new InfoCommand());
		commands.put("stake", new StakeCommand());
		commands.put("sql", new SQLCommand());
		commands.put("stock", new StockCommand());
	}
	
	public void perform(String commandName, MessageReceivedEvent event) {
		
		Command command;
		
		if((command = this.commands.get(commandName.toLowerCase())) != null) {
			command.execute(event);
		}
	}
	
	public HashMap<String, Command> getCommands() {
		return commands;
	}
	
}
