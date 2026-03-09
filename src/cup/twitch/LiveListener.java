package cup.twitch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.github.philippheuer.events4j.simple.SimpleEventHandler;
import com.github.twitch4j.ITwitchClient;
import com.github.twitch4j.TwitchClientBuilder;
import com.github.twitch4j.events.ChannelGoLiveEvent;

import cup.discordbot.DiscordBot;

import jakarta.annotation.PostConstruct;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

@Service
public class LiveListener {
    
    @Value("${twitch.clientId}")
    private String twitchClientId;
    
    @Value("${twitch.clientSecret}")
    private String twitchClientSecret;
    
    @Value("${twitch.channelName}")
    private String channelName;
    
    @Value("${twitch.discord.notificationChannelId}")
    private String notificationChannelId;
    
    @PostConstruct
    public void startTracking() {
        
        TwitchClientBuilder clientBuilder = TwitchClientBuilder.builder();
        
        ITwitchClient twitch = clientBuilder.withClientId(twitchClientId).withClientSecret(twitchClientSecret).withEnableHelix(true).build();
        
        SimpleEventHandler eventHandler = twitch.getEventManager().getEventHandler(SimpleEventHandler.class);
        
        twitch.getClientHelper().enableStreamEventListener(channelName);
        
        eventHandler.onEvent(ChannelGoLiveEvent.class, this::onLiveEvent);
        
        System.out.println("[TWITCH] Started LiveListener");
    }
    
    private void onLiveEvent(ChannelGoLiveEvent event) {
    	System.out.println("[TWITCH] LiveListener triggered for " + channelName);
    	
    	String title = event.getStream().getTitle();
    	String thumbnailUrl = event.getStream().getThumbnailUrl(1280, 720);
    	String game = event.getStream().getGameName();
    	
    	EmbedBuilder eb = new EmbedBuilder();
    	eb.setColor(DiscordBot.EMBEDCOLOR);
    	eb.setAuthor(channelName, "https://twitch.tv/" + channelName);
    	eb.setTitle(title);
    	eb.addField("Category", game, true);
    	eb.setImage(thumbnailUrl + "?t=" + System.currentTimeMillis());
    	
    	TextChannel channel = DiscordBot.INSTANCE.getJDA().getTextChannelById(notificationChannelId);
    	channel.sendMessage("**" + channelName + " is live!**").queue();
        channel.sendMessageEmbeds(eb.build()).addActionRow(Button.link("https://twitch.tv/" + channelName, "Watch livestream")).queue();
    }
    
}
