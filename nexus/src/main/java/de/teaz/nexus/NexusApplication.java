package de.teaz.nexus;

import de.teaz.nexus.auth.service.JwtService;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import de.teaz.nexus.util.CustomEmoji;

@SpringBootApplication
public class NexusApplication {

	public static void main(String[] args) {
        
        CustomEmoji customEmoji = new CustomEmoji();
        customEmoji.load("blackjackcards.emojis");

        TomcatURLStreamHandlerFactory.disable();
        
        SpringApplication.run(NexusApplication.class, args);
	}
}
