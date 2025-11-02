package dao;

import database.Connessione;
import utils.method;
import dto.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ProprietarioD {
	
	//____________________   REGISTRAZIONE     ____________________________________
	
		public static boolean registraP(Proprietario proprietario) { // Registrazione PROPRIETARIO
			Connection conn = null;
			PreparedStatement stmt = null;
			try {
				  conn = Connessione.getConnection();
				  String sql = "INSERT INTO proprietario (nome, cognome, username, psw, Codice_Fiscale) VALUES (?, ?, ?, ?, ?)";
				  stmt = conn.prepareStatement(sql);
				  stmt.setString(1, proprietario.getNome());
				  stmt.setString(2, proprietario.getCognome());
				  stmt.setString(3, proprietario.getUsername());
				  stmt.setString(4, proprietario.getPassword());
				  stmt.setString(5, proprietario.getCodiceFiscale());
				  int rows = stmt.executeUpdate();
				  if (rows ==1) { System.out.println("REGISTRAZIONE AVVENUTA CON SUCCESSO"); return true;}
				  	else {return false;}
			    } catch (SQLException ex) {
				   ex.printStackTrace();
				   return false;
				} finally {
				  try { if (stmt != null) stmt.close(); } catch (Exception e) {}
				  try { if (conn != null) conn.close(); } catch (Exception e) {}
			    }
		  }
				
		public static boolean aggiungiL(Proprietario proprietario) { //Aggiunge il primo lotto disponibile al proprietario
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet risultato = null;
			try {
				 conn = Connessione.getConnection();
				 //controlla l'esistenza di lotti liberi
				 String sql1 = "SELECT ID_Lotto FROM Lotto WHERE Codice_FiscalePr IS NULL LIMIT 1";
				 stmt = conn.prepareStatement(sql1);
				 risultato = stmt.executeQuery();
		
				  if (risultato.next()) {
				      // quando trovo un lotto libero, ricavo l'id
				      int idLottoLibero = risultato.getInt("ID_Lotto");
				            
				      // aggiungo il lotto al proprietario usando il suo codice fiscale
				      String sql2 = "UPDATE Lotto SET Codice_FiscalePr = ? WHERE ID_Lotto = ?";
				      stmt = conn.prepareStatement(sql2);
				      stmt.setString(1, proprietario.getCodiceFiscale());
				      stmt.setInt(2, idLottoLibero);
				      
				     int rows = stmt.executeUpdate();
				     System.out.println("LOTTO AGGIUNTO CON SUCCESSO");
				     return rows == 1; 
				    } else {
				       return false;
				    }
				        
				 } catch (SQLException ex) {
				     ex.printStackTrace();
				     return false;
				} finally {
				   try { if (risultato != null) risultato.close(); } catch (Exception e) {}
				   try { if (stmt != null) stmt.close(); } catch (Exception e) {}
				   try { if (conn != null) conn.close(); } catch (Exception e) {}
				 }
			   }
		
		
			public static ArrayList<String> popolaComboProprietari() { // popola la combobox dei proprietari
			     ArrayList<String> usernames = new ArrayList<>();
			     Connection conn = null;
			     PreparedStatement stmt = null;
			     ResultSet rs = null;

			     try {
			           conn = Connessione.getConnection();

			            String sql = "SELECT username FROM Proprietario";
			            stmt = conn.prepareStatement(sql);
			            rs = stmt.executeQuery();

			            // Aggiungi ogni username all'ArrayList
			            while (rs.next()) { //scorre una riga alla volta del risultato
			                usernames.add(rs.getString("username")); 
			            }
			        } catch (SQLException ex) {
			            ex.printStackTrace(); 
			            System.out.println("Errore durante l'esecuzione della query: " + ex.getMessage());
			        } finally {
			            // Chiudi tutte le risorse nel blocco finally
			            try { if (rs != null) rs.close(); } catch (Exception e) {}
			            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			            try { if (conn != null) conn.close(); } catch (Exception e) {}
			        }

			        return usernames; // Restituisce l'ArrayList con gli username
			    }
			
			
	//____________________   REGISTRAZIONE     ____________________________________
				
}
