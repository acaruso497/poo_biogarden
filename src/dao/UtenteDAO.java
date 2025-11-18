package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class UtenteDAO implements IUtenteDAO{

	//____________________   REGISTRAZIONE     ____________________________________
	
	public boolean usernameEsiste(String username) { //controlla se l'username esistente
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;
	    
	    try {
	        conn = Connessione.getConnection();
	        
	        // Query che cerca lo username in entrambe le tabelle
	        String sql = "SELECT username FROM Proprietario WHERE username = ? " +
	                     "UNION " +
	                     "SELECT username FROM Coltivatore WHERE username = ?";
	        
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1,username);
	        stmt.setString(2,username);
	        
	        rs = stmt.executeQuery();
	        
	        return rs.next(); // Se il ResultSet ha almeno una riga, lo username esiste
	        
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        return false; // In caso di errore, assumiamo che lo username non esista
	    } 
	}
	
	//____________________   REGISTRAZIONE     ____________________________________
	

}
