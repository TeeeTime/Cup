package cup.website;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import cup.util.CustomEmoji;

@SpringBootApplication

@ComponentScan(basePackages = "cup")
public class WebsiteApplication {

	public static void main(String[] args) {
        
        CustomEmoji customEmoji = new CustomEmoji();
        customEmoji.load("blackjackcards.emojis");

        SpringApplication.run(WebsiteApplication.class, args);
	}
}
