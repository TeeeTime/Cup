package cup.website.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.sql.*;

@Service
public class EconomyService {
	
	@Value("${app.db-path}")
    private String dbUrl;
	
	public int getBalance(String discordId) {
        String query = "SELECT balance FROM coins WHERE userid = ?";
        
        try (Connection connection = DriverManager.getConnection(dbUrl);
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
}