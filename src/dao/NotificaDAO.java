package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class NotificaDAO {

	//____________________   CREAZIONE NOTIFICA     ____________________________________
	
	public static boolean Inserisci_NotificaDB(NotificaDTO notifica) { 
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet risultato = null;

    try {
		  conn = Connessione.getConnection(); 
			
		  String sql = "INSERT INTO \"notifica\" (\"attivita_programmate\", \"data_evento\", \"utenti_tag\", \"titolo\", \"descrizione\") " +
				  		"VALUES (?, ?, ?, ?, ?)";
		  stmt = conn.prepareStatement(sql);
		  stmt.setString(1, "");    // Attivita_programmate (vuoto)
		  stmt.setDate(2, notifica.getDataEvento());
		  stmt.setString(3, notifica.getUtentiTag());
		  stmt.setString(4, notifica.getTitolo());
		  stmt.setString(5, notifica.getDescrizione());

		 System.out.println("NOTIFICA INVIATA CON SUCCESSO");
		return stmt.executeUpdate() > 0; // Ritorna true se almeno una riga è stata inserita
	   }catch(SQLException ex) {
		  ex.printStackTrace();
		  return false;
	    }
    finally {
		try { if (risultato != null) risultato.close(); } catch (Exception e) {}
		try { if (stmt != null) stmt.close(); } catch (Exception e) {}
		try { if (conn != null) conn.close(); } catch (Exception e) {}
   }	
 }
	//____________________   CREAZIONE NOTIFICA     ____________________________________
	
	
	

	
	
	
	
}
