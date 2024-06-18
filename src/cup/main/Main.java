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
		/*
		StockManager stockManager = new StockManager();
		stockManager.addStock("BEE", 500, ":bee:");
		stockManager.addStock("FUN", 300, ":roller_coaster:");
		stockManager.addStock("WLD", 600, ":map:");
		stockManager.addStock("TUK", 200, ":auto_rickshaw:");
		stockManager.addStock("BLD", 300, ":construction_site:");
		stockManager.addStock("CRN", 400, ":corn:");
		stockManager.addStock("EGG", 300, ":egg:");
		stockManager.addStock("TEA", 1000, ":tea:");
		stockManager.addStock("BNK", 300, ":bank:");
		stockManager.addStock("STR", 300, ":sparkles:");
		stockManager.startScheduler(180);
		*/
		DiscordBot bot = new DiscordBot(config.getDiscordAPIToken(), config.getCommandPrefix(), config.getAdminId());
		bot.getJDA().getPresence().setActivity(Activity.watching("teeestream"));
	}
}
