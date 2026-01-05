package cup.discordbot.commands;

import org.json.JSONArray;
import org.json.JSONObject;

import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import cup.discordbot.ErrorEmbedBuilder;
import cup.music.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RadioCommand implements Command{

	private static final String BBC_RADIO_1 = "http://as-hls-ww-live.akamaized.net/pool_01505109/live/ww/bbc_radio_one/bbc_radio_one.isml/bbc_radio_one-audio%3d96000.norewind.m3u8";
	
	@Override
	public void execute(MessageReceivedEvent event) {
		
		String[] args = event.getMessage().getContentDisplay().substring(1).trim().split(" ");
		
		PlayerManager playerManager = DiscordBot.INSTANCE.getPlayerManager();
		
		if(args.length == 1) {
			if(playerManager.getPlayer(event.getGuild()) == null) {
				if(event.getMember().getVoiceState().inAudioChannel()) {
					playerManager.loadAndPlay(event.getGuild(), event.getMember().getVoiceState().getChannel().asVoiceChannel(), BBC_RADIO_1);
					
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(DiscordBot.EMBEDCOLOR);
					eb.setDescription("📡 **Started streaming in `" + event.getMember().getVoiceState().getChannel().getName() + "`**\n\n"
							+ "🔴 **Playing:** _`" + getCurrentTrack() + "`_");
					
					event.getChannel().sendMessageEmbeds(eb.build()).queue();
					
				}else {
					event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.notInVoicechannelEmbed().build()).queue();
					
				}
				
			}else {
				playerManager.stopAndClear(event.getGuild());
				
				EmbedBuilder eb = new EmbedBuilder();
				eb.setColor(DiscordBot.EMBEDCOLOR);
				eb.setDescription("❌ **Stoped Streaming**");
				
				event.getChannel().sendMessageEmbeds(eb.build()).queue();
				
			}
			
		}else if(args.length == 2) {
			if(args[1].toLowerCase().equals("info") || args[1].toLowerCase().equals("i")) {
				if(playerManager.getPlayer(event.getGuild()) != null) {
					
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(DiscordBot.EMBEDCOLOR);
					eb.setDescription("🔴 **Playing:** _`" + getCurrentTrack() + "`_");
					
					event.getChannel().sendMessageEmbeds(eb.build()).queue();
				}else {
					event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.notStreamingEmbed().build()).queue();
					
				}
			}else {
				event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
				return;
			}
			
		}else if(args.length == 3) {
			if(args[1].toLowerCase().equals("volume") || args[1].toLowerCase().equals("v")) {
				int volume = 0;
				
				try {
					volume = Integer.parseInt(args[2]);
				} catch(NumberFormatException e) {
					event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.usageEmbed(this).build()).queue();
					return;
				}
				
				if(playerManager.getPlayer(event.getGuild()) != null) {
					playerManager.getPlayer(event.getGuild()).setVolume(volume);
					
					EmbedBuilder eb = new EmbedBuilder();
					eb.setColor(DiscordBot.EMBEDCOLOR);
					eb.setDescription("🔊 **Set volume to `" + volume + "`**");
					
					event.getChannel().sendMessageEmbeds(eb.build()).queue();
				}else {
					event.getChannel().sendMessageEmbeds(ErrorEmbedBuilder.notStreamingEmbed().build()).queue();
				}
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
