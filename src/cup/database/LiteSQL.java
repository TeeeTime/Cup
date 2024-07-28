package cup.database;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LiteSQL {
	
	private static Connection connection;
	private static Statement statement;
	
	public static void connect() {
		connection = null;
		
		try {
			File file = new File("Database.db");
			if(!file.exists()) {
				file.createNewFile();
			}
			
			String url = "jdbc:sqlite:" + file.getPath();
			connection = DriverManager.getConnection(url);
			
			System.out.println("[LiteSQL] Connected to Database");
			
			statement = connection.createStatement();
			
		}catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void disconnect() {
		try {
			if(connection != null) {
				connection.close();
				System.out.println("[LiteSQL] Disconnected from Database!");
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static int onUpdate(String sql) throws Exception{
		
		return statement.executeUpdate(sql);
	}
	
	public static ResultSet onQuery(String sql) throws Exception{

		return statement.executeQuery(sql);
	}
}
