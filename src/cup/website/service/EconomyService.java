package cup.website.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cup.database.LiteSQL;
import cup.discordbot.DiscordBot;
import cup.website.model.LeaderboardEntry;
import net.dv8tion.jda.api.entities.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class EconomyService {
	
	@Value("${app.db-path}")
    private String dbUrl;
	
	public int getBalance(String discordId) {
        String query = "SELECT balance FROM coins WHERE userid = ?";
        
        try(Connection connection = DriverManager.getConnection(dbUrl);
            PreparedStatement statement = connection.prepareStatement(query)) {
            
            statement.setString(1, discordId);
            ResultSet results = statement.executeQuery();
            
            if (results.next()) {
                return results.getInt("balance");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
	}
	
	public List<LeaderboardEntry> getLeaderboard() {
		String query = "SELECT userid, balance FROM coins ORDER BY balance desc LIMIT 10";
		
		List<LeaderboardEntry> leaderboard = new ArrayList<>();
		
		try(Connection connection = DriverManager.getConnection(dbUrl);
			PreparedStatement statement = connection.prepareStatement(query)) {
			
			ResultSet results = statement.executeQuery();
			
			int rank = 1;
			
			while(results.next()) {
				User user = DiscordBot.INSTANCE.getJDA().getUserById(Long.parseLong(results.getString("userid")));
				
				leaderboard.add(new LeaderboardEntry(rank, user.getName(), getBalance(user.getId()), user.getAvatarUrl()));
				
				rank++;
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		
		return leaderboard;
	}
}