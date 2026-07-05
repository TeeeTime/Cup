package de.teaz.nexus;

import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import main.java.de.teaz.nexus.util.CustomEmoji;

@SpringBootApplication
public class WebsiteApplication {

	public static void main(String[] args) {
        
        CustomEmoji customEmoji = new CustomEmoji();
        customEmoji.load("blackjackcards.emojis");

        TomcatURLStreamHandlerFactory.disable();
        
        SpringApplication.run(WebsiteApplication.class, args);
	}
}
