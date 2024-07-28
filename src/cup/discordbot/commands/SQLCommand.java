package cup.discordbot.commands;

import java.awt.Color;
import java.sql.ResultSet;
import java.sql.SQLException;

import cup.database.LiteSQL;
import cup.discordbot.Command;
import cup.discordbot.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class SQLCommand implements Command{

	@Override
	public void execute(MessageReceivedEvent event) {
		if(!event.getAuthor().getId().equals("359013020057206786")) return;
		
		String[] args = event.getMessage().getContentRaw().toLowerCase().split(" ", 2);
		
		EmbedBuilder eb = new EmbedBuilder();
		eb.setColor(Color.WHITE);
		eb.addField("Statement:", "```sql\n" + args[1] + "\n```", false);
		
		if(args[1].startsWith("select")) {
			try {
				ResultSet results = LiteSQL.onQuery("select * from (" + args[1] + ") limit 50");
				eb.addField("Result:", "```" + asStringTable(results) + "```", false);
			}catch(Exception e) {
				eb.addField("Error:", "```ini\n" + e.getMessage() + "\n```", false);
			}
			
		}else {
			try {
				eb.addField("Result:", "```" + LiteSQL.onUpdate(args[1]) + " rows affected```", false);
			} catch (Exception e) {
				eb.addField("Error:", "```ini\n" + e.getMessage() + "\n```", false);
			}
		}
		
		event.getChannel().sendMessageEmbeds(eb.build()).queue();
		
	}

	private String asStringTable(ResultSet results) {
		
		String[][] content = new String[1][1];
		
		String table = "|";
		
		try {
			int columnCount = results.getMetaData().getColumnCount();
			
			for (int i = 1; i <= columnCount; i++) {
			    table += " " + results.getMetaData().getColumnName(i) + " |";
			}
			
			table += "\n\n";
			
			
			content = new String[50][columnCount];
			
			int rowIterator = 0;
			while(results.next()) {
				for (int i = 1; i <= columnCount; i++) {
					if(results.getObject(i) != null) {
						content[rowIterator][i - 1] = results.getObject(i).toString();
					}else {
						content[rowIterator][i - 1] = "null";
					}
				}
				rowIterator++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		for(int i = 0; i < content.length; i++) {
			String row = "|";
			boolean emptyRow = true;
			for(int j = 0; j < content[i].length; j++) {
				String text = content[i][j];
				row += " " + text + " |";
				if(text != null) emptyRow = false;
			}
			
			if(emptyRow) break;
			
			if(table.length() + row.length() + 7 > 1024) {
				table += "\n...";
				break;
			}
			
			table += row + "\n";
		}
		
		return table;
	}
	
	@Override
	public String getUsage() {
		return DiscordBot.INSTANCE.getPrefix() + "sql <execute, query> <statement>";
	}

	@Override
	public String getDescription() {
		return "Access to the database via discord";
	}

	@Override
	public boolean isPublic() {
		return false;
	}

}
