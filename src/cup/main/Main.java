package cup.main;

import cup.database.LiteSQL;
import cup.database.SQLManager;
import cup.discordbot.DiscordBot;
import cup.util.Config;
import net.dv8tion.jda.api.entities.Activity;

public class Main {

	public static void main(String[] args) {
		Config config = new Config();
		
		LiteSQL.connect();
		SQLManager.onCreate();
		
		
		DiscordBot bot = new DiscordBot(config.getDiscordAPIToken(), config.getCommandPrefix(), config.getAdminId());
		bot.getJDA().getPresence().setActivity(Activity.watching("teeestream"));
	}
}
