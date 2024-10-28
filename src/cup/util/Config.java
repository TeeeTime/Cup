package cup.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Config {
	
	private String discordAPIToken;
	private String commandPrefix;
	private String adminId;
	
	public Config() {
		
		File file = new File("config.txt");
		
		if(!file.exists()) {
			System.out.println("[Config] Could not find config file");
			createConfigFile(file);
			
			System.out.println("[System] Shutting down...");
			System.exit(0);
		}
		
		try {
			Scanner scanner = new Scanner(file);
			
			discordAPIToken = scanner.nextLine().substring(18).trim();
			commandPrefix = scanner.nextLine().substring(15).trim();
			adminId = scanner.nextLine().substring(9).trim();
			
			System.out.println("[Config] Command Prefix: " + commandPrefix);
			System.out.println("[Config] Admin Id: " + adminId);
			
			scanner.close();
			
		} catch(IOException e) {
			System.out.println("[Config] An error occurred while trying to read the config file");
			
			e.printStackTrace();
			
			System.exit(0);
		}
	}
	
	private void createConfigFile(File file) {
		
		try {
			file.createNewFile();
			
			FileWriter fileWriter = new FileWriter(file);
			fileWriter.write("Discord API Token:\nCommand Prefix:\nAdmin Id:");
			fileWriter.close();
			System.out.println("[Config] New config file has been created");
			
		} catch(IOException e) {
			System.out.println("[Config] An error occurred while trying to create a new config file");
			
			e.printStackTrace();
			
			System.exit(0);
		}
	}
	
	public String getDiscordAPIToken() {
		return discordAPIToken;
	}
	
	public String getCommandPrefix() {
		return commandPrefix;
	}
	
	public String getAdminId() {
		return adminId;
	}


}
