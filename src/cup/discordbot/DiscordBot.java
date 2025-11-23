package cup.discordbot;

import cup.discordbot.commands.BlackjackCommand;
import cup.discordbot.commands.RaceCommand;
import cup.discordbot.commands.RockPaperScissorsCommand;
import cup.discordbot.commands.RulesCommand;
import cup.games.BlackjackManager;
import cup.games.RaceManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class DiscordBot {
	
	public static DiscordBot INSTANCE;
	
	private JDA jda;
	
	private CommandManager commandManager;
	
	private String prefix;
	
	private String adminId;
	
	private RaceManager raceManager;
	
	private BlackjackManager blackjackManager;
	
	private String chatGPTToken;
	
	public DiscordBot(String token, String prefix, String adminId, String chatGPTToken) {
		
		INSTANCE = this;
		
		this.prefix = prefix;
		this.adminId = adminId;
		this.chatGPTToken = chatGPTToken;
		
		jda = JDABuilder.createDefault(token)
				.enableIntents(GatewayIntent.MESSAGE_CONTENT)
				.enableIntents(GatewayIntent.GUILD_MESSAGES)
				.enableIntents(GatewayIntent.GUILD_MEMBERS)
				.enableIntents(GatewayIntent.GUILD_PRESENCES)
				.enableIntents(GatewayIntent.GUILD_VOICE_STATES)
				.enableCache(CacheFlag.ONLINE_STATUS)
				.enableCache(CacheFlag.ACTIVITY)
				.addEventListeners(new CommandListener())
				.addEventListeners(new RockPaperScissorsCommand())
				.addEventListeners(new RulesCommand())
				.addEventListeners(new RaceCommand())
				.addEventListeners(new BlackjackCommand())
				.addEventListeners(new CounterstrikeChangelogListener())
				.addEventListeners(new AIListener())
				.addEventListeners(new VoiceSessionListener())
				.setAutoReconnect(true)
				.build();
		
		System.out.println("[DISCORD] BOT online as " + jda.getSelfUser().getName());
		
		commandManager = new CommandManager();
		
		raceManager = new RaceManager();
		blackjackManager = new BlackjackManager();
		
	}
	
	public JDA getJDA() {
		return jda;
	}

	public CommandManager getCommandManager() {
		return commandManager;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getAdminId() {
		return adminId;
	}

	public RaceManager getRaceManager() {
		return raceManager;
	}

	public BlackjackManager getBlackjackManager() {
		return blackjackManager;
	}

	public String getChatGPTToken() {
		return chatGPTToken;
	}
}
