package cup.main;

import cup.database.LiteSQL;
import cup.database.SQLManager;
import cup.discordbot.DiscordBot;
import cup.util.Config;
import cup.util.CustomEmoji;
import net.dv8tion.jda.api.entities.Activity;

public class Main {

	public static void main(String[] args) {
		
		Config config = new Config();
		
		CustomEmoji customEmoji = new CustomEmoji();
		customEmoji.load("blackjackcards.emojis");
		
		LiteSQL.connect();
		SQLManager.onCreate();
		
		
		DiscordBot bot = new DiscordBot(config.getDiscordAPIToken(), config.getCommandPrefix(), config.getAdminId(), config.getChatGPTAPIToken());
		bot.getJDA().getPresence().setActivity(Activity.customStatus("3000 lines of shit code"));
	}
}
