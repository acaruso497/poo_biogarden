package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class ColtivatoreDAO {
	
	//____________________   LOGIN     ____________________________________
	
	public static boolean authC(UtenteDTO User) { //Autenticazione coltivatore
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
	
	//____________________   LOGIN     ____________________________________
	
	
	//____________________   REGISTRAZIONE     ____________________________________
		
	public static boolean registraC(ColtivatoreDTO coltivatore) {		// Registrazione COLTIVATORE
			    Connection conn = null;
			    PreparedStatement stmt = null;
			    try {
			        conn = Connessione.getConnection();

			        String sql = """
			            INSERT INTO Coltivatore 
			            (Codice_Fiscale, nome, cognome, username, psw, esperienza, username_proprietario)
			            VALUES (?, ?, ?, ?, ?, 'principiante', ?)
			        """;

			        stmt = conn.prepareStatement(sql);
			        stmt.setString(1, coltivatore.getCodiceFiscale());
			        stmt.setString(2, coltivatore.getNome());
			        stmt.setString(3, coltivatore.getCognome());
			        stmt.setString(4, coltivatore.getUsername());
			        stmt.setString(5, coltivatore.getPassword());
			        stmt.setString(6, coltivatore.getUsernameProprietario());

			        int rows = stmt.executeUpdate(); //esegue l'INSERT e restituisce la riga inserita
			        System.out.println("REGISTRAZIONE AVVENUTA CON SUCCESSO");
			        return rows == 1; //se è stata inserita una riga, ritorna true
			        

			    } catch (SQLException ex) {
			        ex.printStackTrace();
			        return false;
			    } finally {
			        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			        try { if (conn != null) conn.close(); } catch (Exception e) {}
			    }
			}
	
	//____________________   REGISTRAZIONE     ____________________________________
}
