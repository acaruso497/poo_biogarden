
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connessione {
	
	public static Connection getConnection() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/BioGardenUnina";
        String user = "postgres";
        String password = "Admin"; 

        return DriverManager.getConnection(url, user, password);
    }
}