package cup.discordbot;

import java.awt.Color;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cup.discordbot.commands.BlackjackCommand;
import cup.discordbot.commands.RaceCommand;
import cup.discordbot.commands.RadioCommand;
import cup.discordbot.commands.RockPaperScissorsCommand;
import cup.discordbot.commands.RulesCommand;
import cup.games.BlackjackManager;
import cup.games.RaceManager;
import dev.arbjerg.lavalink.client.LavalinkClient;
import dev.arbjerg.lavalink.client.NodeOptions;
import dev.arbjerg.lavalink.libraries.jda.JDAVoiceUpdateListener;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

@Service
public class DiscordBot {
	
	public static DiscordBot INSTANCE;
	
	private JDA jda;
	
	@Value("${discord.bot.token}")
	private String token;
	
	@Value("${discord.bot.id}")
	private long id;
	
	@Value("${discord.bot.prefix}")
	private String prefix;
	
	@Value("${discord.bot.admin-id}")
	private String adminId;
	
	@Value("${discord.bot.chatgpt-token}")
	private String chatGPTToken;
	
	private CommandManager commandManager;

	private RaceManager raceManager;
	
	private BlackjackManager blackjackManager;
	
	private LavalinkClient lavalink;
	
	public static final Color EMBEDCOLOR = Color.getHSBColor(46, 100, 47);
	
	public DiscordBot() {
		INSTANCE = this;
	}
	
	@PostConstruct
	public void startBot() {
		this.commandManager = new CommandManager();
		this.raceManager = new RaceManager();
		this.blackjackManager = new BlackjackManager();
		
		JDABuilder builder = JDABuilder.createDefault(token);
		
		LavalinkClient lavalink = new LavalinkClient(id);
		
		lavalink.addNode(new NodeOptions.Builder()
		        .setName("LocalNode")
		        .setServerUri("ws://localhost:2333")
		        .setPassword("youshallnotpass")
		        .build());
		
		this.lavalink = lavalink;
		
		jda = builder
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
				.addEventListeners(new GuildJoinListener())
				.addEventListeners(new RadioCommand())
				.setAutoReconnect(true)
				.setVoiceDispatchInterceptor(new JDAVoiceUpdateListener(lavalink))
				.build();
		
		jda.getPresence().setActivity(Activity.customStatus("Check out teaz.fun"));
		
		System.out.println("[DISCORD] BOT online as " + jda.getSelfUser().getName());
	}
	
	@PreDestroy
    public void stopBot() {
        if (jda != null) {
            System.out.println("[DISCORD] Shutting down...");
            jda.shutdownNow();
        }
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
	
	public LavalinkClient getLavalink() {
		return lavalink;
	}
}
