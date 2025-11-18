package dao;

import database.Connessione;
import dto.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProprietarioDAO implements IProprietarioDAO {
	
	//____________________   LOGIN     ____________________________________
	
	public boolean authP(UtenteDTO User) { //Autenticazione proprietario
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
		   } 
	}
	
	public ProprietarioDTO getProprietario(ProprietarioDTO credenziali) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet risultato = null;
	    try {
	        conn = Connessione.getConnection();
	        // Cerca il proprietario con username e password forniti
	        String sql = "SELECT * FROM proprietario WHERE username = ? AND psw = ?";
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, credenziali.getUsername());
	        stmt.setString(2, credenziali. getPassword());
	        risultato = stmt.executeQuery();
	        
	        if (risultato.next()) {
	            // Quando trovato, crea e popola un nuovo ProprietarioDTO con tutti gli attributi
	            ProprietarioDTO proprietario = new ProprietarioDTO(
	                risultato.getString("username"),
	                risultato.getString("psw"),
	                risultato.getString("nome"),
	                risultato.getString("cognome"),
	                risultato.getString("codice_fiscale")
	            );            
	            System.out.println("PROPRIETARIO RECUPERATO CON SUCCESSO");
	            return proprietario;
	        } else {
	        	System.out.println("RECUPERO PROPRIETARIO FALLITO \n funzione di ProprietarioDAO\n chiamata :getProprietario");
	            return null; 
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        return null;
	    } 
	}
	//____________________   LOGIN     ____________________________________
	
	
	//____________________   REGISTRAZIONE     ____________________________________
	
		public boolean registraP(ProprietarioDTO proprietario) { // Registrazione PROPRIETARIO
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
				} 
		  }		
		
		public boolean aggiungiL(ProprietarioDTO proprietario) { //Aggiunge il primo lotto disponibile al proprietario
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet risultato = null;
			try {
				 conn = Connessione.getConnection();
				 
				 String sql1 = "SELECT ID_Lotto FROM Lotto WHERE Codice_FiscalePr IS NULL LIMIT 1"; //controlla l'esistenza di lotti liberi
				 stmt = conn.prepareStatement(sql1);
				 risultato = stmt.executeQuery();
		
				  if (risultato.next()) {
				      int idLottoLibero = risultato.getInt("ID_Lotto"); // quando trovo un lotto libero, ricavo l'id
				            
				      String sql2 = "UPDATE Lotto SET Codice_FiscalePr = ? WHERE ID_Lotto = ?"; // aggiungo il lotto al proprietario usando il suo codice fiscale
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
				} 
			   }
		
			public ArrayList<String> popolaComboProprietari() { // popola la combobox dei proprietari
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
			        } 

			        return usernames; // Restituisce l'ArrayList con gli username
			    }						
	//____________________   REGISTRAZIONE     ____________________________________
				
		
	//____________________   CREAZIONE NOTIFICA     ____________________________________
			
			public String getDestinatariUsernamesByProprietario(ProprietarioDTO proprietario) {
			    Connection conn = null;
			    PreparedStatement stmt = null;
			    ResultSet risultato = null;
			    try {
			        conn = Connessione.getConnection();
			        // Tutti i coltivatori che lavorano su QUALSIASI lotto del proprietario indicato
			        String sql =
			                "SELECT COALESCE(string_agg(DISTINCT c.username, ',' ORDER BY c.username), '') AS usernames " +
			                 "FROM proprietario p " +
			                 "JOIN coltivatore c ON c.username_proprietario = p.username " +
			                 "WHERE p.username = ?";
			        stmt = conn.prepareStatement(sql);
			        stmt.setString(1, proprietario.getUsername());
			        risultato = stmt.executeQuery();
			        
			        if (risultato.next()) {
			            String s = risultato.getString(1);
			            return s != null ? s : "";
			        }
			        return "";
			    } catch (SQLException ex) {
			        ex.printStackTrace();
			        return "";
			    } 
			}		
			
			public ArrayList<String> getColtivatoriByProprietario(ProprietarioDTO proprietario) { 
				ArrayList<String> coltivatori = new ArrayList<>();
			    Connection conn = null;
			    PreparedStatement stmt = null;
			    ResultSet risultato = null;
			    try {
			        conn = Connessione.getConnection();
			        // Tutti i coltivatori che lavorano su QUALSIASI lotto del proprietario indicato
			        String sql =
			                "SELECT DISTINCT c.username " +
			                 "FROM proprietario p " +
			                 "JOIN coltivatore c ON c.username_proprietario = p.username " +
			                 "WHERE p.username = ?";
			        stmt = conn.prepareStatement(sql);
			        stmt.setString(1, proprietario.getUsername());
			        risultato = stmt.executeQuery();
			        while (risultato.next()) {
			            String coltivatore = risultato.getString("username");
			            coltivatori.add(coltivatore);
			        }			        
			        return coltivatori;
			    } catch (SQLException ex) {
			        ex.printStackTrace();
			    } 
			    return coltivatori;
			}
			
	//____________________   CREAZIONE NOTIFICA     ____________________________________
			
	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________	
			
			public List<String> getLottiByProprietario(ProprietarioDTO proprietario) { //recupera i lotti di un proprietario (utile per popolare ComboLotti)
			    List<String> lista = new ArrayList<>(); // Lista vuota per ID lotti
			    Connection conn = null;
			    PreparedStatement stmt = null;
			    ResultSet risultato = null;
			    try {
			        conn = Connessione.getConnection(); 

			        String sql = "SELECT l.ID_Lotto, l.posizione " + 
			                    "FROM Lotto l " +
			                    "JOIN Proprietario p ON l.Codice_FiscalePr = p.Codice_Fiscale " +
			                    "WHERE p.username = ? " +
			                    "ORDER BY l.posizione"; 

			        stmt = conn.prepareStatement(sql);   
			        stmt.setString(1, proprietario.getUsername());  // Inserisce l'username del proprietario
			        risultato = stmt.executeQuery();

			        while (risultato.next()) {
			            int idLotto = risultato.getInt("ID_Lotto"); // Legge solo l'ID
		                String idStr = String.valueOf(idLotto); 
		                lista.add(idStr); // Aggiunge alla lista
			        }
			    } catch (SQLException e) {
			        System.err.println("Errore SELECT: " + e.getMessage());
			    } 
			    return lista;
			}		
	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________
			

//          _________________ VISUALIZZA PROGETTI _________________
			
		    public List<String> getProgettiByProprietario(ProprietarioDTO proprietario) { //seleziono tutti i progetti del proprietario dato il suo username (utile per ComboProgetto)
		        List<String> lista = new ArrayList<>();
		        Connection conn = null;
		        PreparedStatement stmt = null;
		        ResultSet risultato = null;

		        try {
		            conn = Connessione.getConnection(); 

			        String sql = "SELECT pc.titolo " +
				        		  "FROM Progetto_Coltivazione pc " +
				        		  "JOIN Lotto l ON l.ID_Lotto = pc.ID_Lotto " +
				        		  "JOIN Proprietario p ON l.Codice_FiscalePr = p.Codice_Fiscale " +
				        		  "WHERE p.username = ? " +
				        		  "ORDER BY pc.ID_Progetto ";
		            
		            stmt = conn.prepareStatement(sql);   
		            stmt.setString(1, proprietario.getUsername());
		            risultato = stmt.executeQuery();
		            
		            while (risultato.next()) {
		                String titoloProgetto = risultato.getString("titolo");
		                lista.add(titoloProgetto);
		            }  
		        } catch (SQLException ex) {
		        	ex.printStackTrace();
		        } 
		        return lista;
		    }
		    
		    
//          _________________ VISUALIZZA PROGETTI _________________
}

