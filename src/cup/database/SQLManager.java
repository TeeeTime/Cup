package cup.database;

public class SQLManager {
	
	public static void onCreate() {
		try {
			LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS coins(userid STRING, balance INTEGER)");
			LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS daily(userid STRING, lastclaimdate STRING)");
			LiteSQL.onUpdate("CREATE TABLE IF NOT EXISTS stats(statid STRING, userid STRING, value INTEGER)");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
