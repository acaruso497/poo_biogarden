package dao;
import database.Connessione;
import utils.method;
import dto.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class LoginD {
	
	//____________________   LOGIN     ____________________________________
	
			public static boolean authP(utente User) { //Autenticazione proprietario
				Connection conn = null;
				PreparedStatement stmt = null;
				ResultSet risultato = null;
				try {
						conn = Connessione.getConnection(); 
						String sql = "SELECT * FROM proprietario WHERE username=? AND psw=?";
						stmt = conn.prepareStatement(sql);
						stmt.setString(1, User.getUsername());
						stmt.setString(2, User.getPassword());
						risultato = stmt.executeQuery();
						
						if(risultato.next()) {
							System.out.println(" ACCESSO CONSENTITO: PROPRIETARIO");
							return true;
						}else {
							System.out.println(" ACCESSO NON CONSENTITO: PROPRIETARIO");
							return false;
						}
				   } catch(SQLException ex) {
					   ex.printStackTrace();
					   return false;
				   } finally {
					   try { if (risultato != null) risultato.close(); } catch (Exception e) {}
				        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
				        try { if (conn != null) conn.close(); } catch (Exception e) {}
				   }
				
			}
			
			public static boolean authC(utente User) { //Autenticazione coltivatore
				Connection conn = null;
				PreparedStatement stmt = null;
				ResultSet risultato = null;
				try {
						conn = Connessione.getConnection(); 
						String sql = "SELECT * FROM coltivatore WHERE username=? AND psw=?";
						stmt = conn.prepareStatement(sql);
						stmt.setString(1,User.getUsername());
						stmt.setString(2, User.getPassword());
						risultato = stmt.executeQuery();
						if(risultato.next()) {
							System.out.println(" ACCESSO CONSENTITO: COLTIVATORE");
							return true;
						}else {
							System.out.println(" ACCESSO NON CONSENTITO: COLTIVATORE");
							return false;
						}
				   } catch(SQLException ex) {
					   ex.printStackTrace();
					   return false;
				   } finally {
					   try { if (risultato != null) risultato.close(); } catch (Exception e) {}
				        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
				        try { if (conn != null) conn.close(); } catch (Exception e) {}
				   }
				
			}
			
			public static boolean usernameEsiste(utente User) { //controlla se l'username esistente
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
			        stmt.setString(1, User.getUsername());
			        stmt.setString(2, User.getUsername());
			        
			        rs = stmt.executeQuery();
			        
			        // Se il ResultSet ha almeno una riga, lo username esiste
			        return rs.next();
			        
			    } catch (SQLException ex) {
			        ex.printStackTrace();
			        return false; // In caso di errore, assumiamo che lo username non esista
			    } finally {
			        // Chiudi le risorse
			        try { if (rs != null) rs.close(); } catch (Exception e) {}
			        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			        try { if (conn != null) conn.close(); } catch (Exception e) {}
			    }
			}
			
			
			public static Object creazioneUtente(utente User) { // crea l'oggetto proprietario/coltivatore per il login
				Connection conn = null;
			    PreparedStatement stmt = null;
			    ResultSet rs = null;
				
			    try {
			    	conn = Connessione.getConnection();
			    	
			    	String sql1 = "SELECT * FROM Proprietario WHERE username = ?";
			    	stmt = conn.prepareStatement(sql1);
			    	stmt.setString(1, User.getUsername());
			    	rs = stmt.executeQuery();
			    	
			    	if (rs.next()) {
			            Proprietario proprietario = new Proprietario( //restituisce tutti i campi della select e crea l'oggetto da restituire
			                rs.getString("nome"),
			                rs.getString("cognome"),
			                rs.getString("username"),
			                rs.getString("psw"),
			                rs.getString("codice_fiscale")  
			            );
			            return proprietario; //restituisce l'oggetto proprietario
			        }
			    	
			    	String sql2 = "SELECT * FROM Coltivatore WHERE username = ?";
			    	stmt = conn.prepareStatement(sql2);
			    	stmt.setString(1, User.getUsername());
			    	rs = stmt.executeQuery();
			    	
			    	if (rs.next()) {
			            Coltivatore coltivatore = new Coltivatore( 
			            		rs.getString("nome"),
				                rs.getString("cognome"),
				                rs.getString("username"),
				                rs.getString("psw"),
				                rs.getString("codice_fiscale"),
				                rs.getString("username_proprietario")
			            );
			            return coltivatore; //restituisce l'oggetto coltivatore
			        }
			    	
			    	return null; //non trova nessun utente
			    	
			    } catch (SQLException ex) {
			        ex.printStackTrace();
			        return null; 
			    } finally {
			        try { if (rs != null) rs.close(); } catch (Exception e) {}
			        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			        try { if (conn != null) conn.close(); } catch (Exception e) {}
			    }
				
			}
}
