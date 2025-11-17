package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class UtenteDAO {

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
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}
	
	//____________________   REGISTRAZIONE     ____________________________________
	
	//____________________   LOGIN     ____________________________________
	
//	public Object creazioneUtente(UtenteDTO User) { // crea l'oggetto proprietario/coltivatore per il login
//		Connection conn = null;
//	    PreparedStatement stmt = null;
//	    ResultSet rs = null;
//		
//	    try {
//	    	conn = Connessione.getConnection();
//	    	
//	    	String sql1 = "SELECT * FROM Proprietario WHERE username = ?";
//	    	stmt = conn.prepareStatement(sql1);
//	    	stmt.setString(1, User.getUsername());
//	    	rs = stmt.executeQuery();
//	    	
//	    	if (rs.next()) {
//	    		ProprietarioDTO proprietario = new ProprietarioDTO( //restituisce tutti i campi della select e crea l'oggetto da restituire
//	                rs.getString("nome"),
//	                rs.getString("cognome"),
//	                rs.getString("username"),
//	                rs.getString("psw"),
//	                rs.getString("codice_fiscale")  
//	            );
//	            return proprietario; //restituisce l'oggetto proprietario
//	        }
//	    	
//	    	String sql2 = "SELECT * FROM Coltivatore WHERE username = ?";
//	    	stmt = conn.prepareStatement(sql2);
//	    	stmt.setString(1, User.getUsername());
//	    	rs = stmt.executeQuery();
//	    	
//	    	if (rs.next()) {
//	            ColtivatoreDTO coltivatore = new ColtivatoreDTO( 
//	            		rs.getString("nome"),
//		                rs.getString("cognome"),
//		                rs.getString("username"),
//		                rs.getString("psw"),
//		                rs.getString("codice_fiscale"),
//		                rs.getString("username_proprietario")
//	            );
//	            return coltivatore; //restituisce l'oggetto coltivatore
//	        }
//	    	
//	    	return null; //non trova nessun utente
//	    	
//	    } catch (SQLException ex) {
//	        ex.printStackTrace();
//	        return null; 
//	    } finally {
//	        try { if (rs != null) rs.close(); } catch (Exception e) {}
//	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
//	        try { if (conn != null) conn.close(); } catch (Exception e) {}
//	    }
//		
//	}
	
	//____________________   LOGIN     ____________________________________
}
