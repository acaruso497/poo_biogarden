package dao;
import database.Connessione;
import utils.method;
import dto.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class LoginD {
	
	//____________________   LOGIN     ____________________________________
	
			//Autenticazione proprietario
			public static boolean authP(utente User) {
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
							return true;
						}else {
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
			
			//Autenticazione coltivatore
			public static boolean authC(utente User) {
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
							System.out.println(" ACCESSO CONSENTITO");
							return true;
						}else {
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
		
			public static String getCodiceFiscaleByUsername(String username) {
			    String codiceFiscale = null;
			    Connection conn = null;
			    PreparedStatement stmt = null;
			    ResultSet rs = null;

			    try {
			        // Ottieni la connessione al database (assumo che Connessione sia una classe di utilità)
			        conn = Connessione.getConnection();

			        // Query SQL diretta sulla tabella Proprietario
			        String sql = "SELECT Codice_Fiscale FROM Proprietario WHERE username = ?";
			        stmt = conn.prepareStatement(sql);
			        stmt.setString(1, username); // Imposta il parametro username

			        // Esegui la query
			        rs = stmt.executeQuery();

			        // Recupera il risultato
			        if (rs.next()) {
			            codiceFiscale = rs.getString("Codice_Fiscale"); // Recupera il Codice_Fiscale
			        }
			    } catch (SQLException ex) {
			        ex.printStackTrace(); 
			    } finally {
			        // Chiudi tutte le risorse nel blocco finally
			        try { if (rs != null) rs.close(); } catch (Exception e) {}
			        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			        try { if (conn != null) conn.close(); } catch (Exception e) {}
			    }

			    return codiceFiscale; // Restituisce il Codice_Fiscale o null se non trovato
			}
}
