package cup.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class CustomEmoji {
	
	static HashMap<String, String> emojis;
	
	public CustomEmoji() {
		emojis = new HashMap<String, String>();
	}

	public static String getFormated(String name) {
		return emojis.get(name);
	}
	
	public void load(String filePath) {
		File file = new File(filePath);
		
		if(!file.exists()) {
			System.out.println("[EmojiLoader] Could not find file \"" + filePath + "\"");
			
			System.out.println("[System] Shutting down...");
			System.exit(0);
		}
		
		try {
			Scanner scanner = new Scanner(file);
			
			while(scanner.hasNextLine()) {
				String[] args = scanner.nextLine().split(": ");
				emojis.put(args[0], args[1]);
			}
			
			scanner.close();
			
		} catch(IOException e) {
			System.out.println("[EmojiLoader] An error occurred while trying to read \"" + filePath + "\"");
			
			e.printStackTrace();
			
			System.exit(0);
		}
	}
}
