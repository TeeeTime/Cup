package cup.discordbot.commands;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import dev.arbjerg.lavalink.client.Link;
import dev.arbjerg.lavalink.client.LinkState;
import dev.arbjerg.lavalink.client.player.TrackLoaded;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RadioCommand extends ListenerAdapter implements Command{

	private static HashMap<String, String> stations = new HashMap<>();
	
	//TODO: Add more stations, maybe move radio station registry to its own Object
	static {
		stations.put("bbc_radio_one", "http://as-hls-ww-live.akamaized.net/pool_01505109/live/ww/bbc_radio_one/bbc_radio_one.isml/bbc_radio_one-audio%3d96000.norewind.m3u8");
		stations.put("bbc_radio_one_dance", "http://as-hls-ww-live.akamaized.net/pool_62063831/live/ww/bbc_radio_one_dance/bbc_radio_one_dance.isml/bbc_radio_one_dance-audio%3d96000.norewind.m3u8");
		stations.put("bbc_radio_two", "http://as-hls-ww-live.akamaized.net/pool_74208725/live/ww/bbc_radio_two/bbc_radio_two.isml/bbc_radio_two-audio%3d96000.norewind.m3u8");
	}
	
	private static final String BBC_RADIO_1 = "http://as-hls-ww-live.akamaized.net/pool_01505109/live/ww/bbc_radio_one/bbc_radio_one.isml/bbc_radio_one-audio%3d96000.norewind.m3u8";
	
	@Override
	public void execute(MessageReceivedEvent event) {
		
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		if(args.length == 1) {
			if(!event.getMember().getVoiceState().inAudioChannel()) {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.notInVoicechannelEmbed().build()).queue();
				return;
			}
			
			Link existingLink = DiscordBot.INSTANCE.getLavalink().getLinkIfCached(event.getGuild().getIdLong());
			
			if (existingLink != null && existingLink.getState() == LinkState.CONNECTED) {
			    existingLink.destroy().subscribe(); 
			    
			    event.getGuild().getAudioManager().closeAudioConnection();
			    
			    EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(DiscordBot.EMBEDCOLOR);
				eb.setDescription("❌ **Stoped streaming**");
				
				event.getChannel().sendMessageEmbeds(eb.build()).queue();
			    return;
			}
			
			Link link = DiscordBot.INSTANCE.getLavalink().getOrCreateLink(event.getGuild().getIdLong());
			
			event.getGuild().getAudioManager().openAudioConnection(event.getMember().getVoiceState().getChannel().asVoiceChannel());

			link.loadItem(BBC_RADIO_1).subscribe(item -> {
				TrackLoaded trackLoaded = (TrackLoaded) item;
		        
		        link.createOrUpdatePlayer()
		            .setTrack(trackLoaded.getTrack())
		            .setVolume(20)
		            .subscribe();
			});
			
			EmbedBuilder eb = new EmbedBuilder();
			eb.setColor(DiscordBot.EMBEDCOLOR);
			eb.setDescription("📡 **Started streaming in `" + event.getMember().getVoiceState().getChannel().getName() + "`**\n\n"
					+ "🔴 **Playing:** _`" + getCurrentTrack() + "`_");
			
			event.getChannel().sendMessageEmbeds(eb.build()).queue();
			
			return;
			
		}else if(args.length == 2) {
			if(args[1].toLowerCase().equals("info") || args[1].toLowerCase().equals("i")) {
				Link existingLink = DiscordBot.INSTANCE.getLavalink().getLinkIfCached(event.getGuild().getIdLong());
				
				if (existingLink != null && existingLink.getState() == LinkState.CONNECTED) {
					
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(DiscordBot.EMBEDCOLOR);
					eb.setDescription("🔴 **Playing:** _`" + getCurrentTrack() + "`_");
					
					event.getChannel().sendMessageEmbeds(eb.build()).queue();
				}else {
					event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.notStreamingEmbed().build()).queue();
					
				}
			}else if(args[1].toLowerCase().equals("set") || args[1].toLowerCase().equals("s")){
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(DiscordBot.EMBEDCOLOR);
				eb.setDescription("📻 **Select from the following stations:**");
				
				StringSelectMenu menu = StringSelectMenu.create("radio_station_select-" + event.getAuthor().getId())
					    .setPlaceholder("Choose a radio station...")
					    .addOption("BBC Radio 1", "bbc_radio_one", "Contemporary pop, dance, and rock")
					    .addOption("BBC Radio 1 Dance", "bbc_radio_one_dance", "Electronic, house, and dance music")
					    .addOption("BBC Radio 2", "bbc_radio_two", "Adult contemporary, classic hits, and entertainment")
					    .build();
				
				event.getChannel().sendMessageEmbeds(eb.build()).addComponents(ActionRow.of(menu)).queue();
			
			}else {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
				return;
			}
		}else {
			event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
			return;
		}
	}
	
	@Override
	public void onStringSelectInteraction(StringSelectInteractionEvent event) {
	    if (event.getComponentId().startsWith("radio_station_select")) {
	    	String ownerId = event.getComponentId().split("-")[1];
	        
	    	if(!event.getUser().getId().equals(ownerId)) {
	    		event.deferEdit().queue(); 
	    		return;
	    	}
	    	
	        String selectedUrl = stations.get(event.getValues().get(0)); 
	        
	        Link link = DiscordBot.INSTANCE.getLavalink().getLinkIfCached(event.getGuild().getIdLong());
			
			if (link != null && link.getState() == LinkState.CONNECTED) {
				link.loadItem(selectedUrl).subscribe(item -> {
				    
				    if (item instanceof TrackLoaded) {
				        TrackLoaded trackLoaded = (TrackLoaded) item;
				        
				        link.createOrUpdatePlayer()
				            .setTrack(trackLoaded.getTrack())
				            .subscribe();
				        
				        event.deferEdit().queue(); 
				    } else {
				        event.editMessage("❌ Unexpected error loading the stream.").setComponents().queue();
				    }
				});
				
			}else {
				event.editMessageEmbeds(ErrorEmbedBuilder.notStreamingEmbed().build()).setComponents().queue();
			}
	        
	    }
	}

	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "radio [volume 0-100]";
	}

	@Override
	public String getDescription() {
		return "Joins your current voicechannel and plays BBC Radio 1";
	}

	@Override
	public boolean isPublic() {
		return true;
	}
	
	//TODO: Implement logic for different stations
	private String getCurrentTrack() {
	    String url = "https://rms.api.bbc.co.uk/v2/services/bbc_radio_one/segments/latest";
	    
	    OkHttpClient client = new OkHttpClient.Builder()
	        .callTimeout(5, java.util.concurrent.TimeUnit.SECONDS)
	        .build();

	    Request request = new Request.Builder()
	        .url(url)
	        .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
	        .build();

	    try (Response response = client.newCall(request).execute()) {
	        if (!response.isSuccessful() || response.body() == null) {
	            System.err.println("BBC API Failed: " + response.code());
	            return "BBC Radio 1 (Live)";
	        }

	        String jsonData = response.body().string();
	        JSONObject root = new JSONObject(jsonData);

	        if (!root.has("data")) return "BBC Radio 1";
	        
	        JSONArray dataArray = root.getJSONArray("data");
	        if (dataArray.isEmpty()) return "BBC Radio 1";

	        JSONObject currentItem = dataArray.getJSONObject(0);

	        if (currentItem.has("titles")) {
	            JSONObject titles = currentItem.getJSONObject("titles");
	            String primary = titles.optString("primary", "");     // Song Title
	            String secondary = titles.optString("secondary", ""); // Artist Name
	            
	            if (!primary.isEmpty() && !secondary.isEmpty()) {
	                return secondary + " - " + primary;
	            }
	            
	            if (!primary.isEmpty()) {
	                return "Show: " + primary;
	            }
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    
	    return "BBC Radio 1 (Stream)";
	}

}
