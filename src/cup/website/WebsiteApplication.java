package cup.website;

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import cup.util.CustomEmoji;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

@ComponentScan(basePackages = "cup")
public class WebsiteApplication {

	public static void main(String[] args) {
        
        CustomEmoji customEmoji = new CustomEmoji();
        customEmoji.load("blackjackcards.emojis");

        TomcatURLStreamHandlerFactory.disable();
        
        SpringApplication.run(WebsiteApplication.class, args);
	}
}
