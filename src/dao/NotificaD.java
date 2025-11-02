package dao;

import database.Connessione;
import dto.*;
import java.sql.*;
import java.util.ArrayList;

public class NotificaD {

	public static boolean Inserisci_NotificaDB(Notifica notifica) { 
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
	
	public static String getDestinatariUsernamesByProprietario(Proprietario proprietario) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet risultato = null;

	    try {
	        conn = Connessione.getConnection();

	        // Tutti i coltivatori che lavorano su QUALSIASI lotto del proprietario indicato
	        String sql =
	                "SELECT COALESCE(string_agg(DISTINCT c.username, ',' ORDER BY c.username), '') AS usernames " +
	                        "FROM proprietario p " +
	                        "JOIN lotto l       ON l.codice_fiscalepr = p.codice_fiscale " +
	                        "JOIN attivita a    ON a.id_lotto = l.id_lotto " +
	                        "JOIN coltivatore c ON c.codice_fiscale = a.codice_fiscalecol " +
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
	    } finally {
	        try { if (risultato != null) risultato.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}
	
	public boolean ciSonoNotificheNonLette(Coltivatore coltivatore) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conn = Connessione.getConnection();

	        String sql = 
	        	    "SELECT COUNT(*) " +
	        	    "FROM \"notifica\" " +
	        	    "WHERE \"lettura\" = FALSE " +
	        	    "  AND \"utenti_tag\" LIKE ?";
	        
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, "%" + coltivatore.getUsername() + "%");

	        rs = stmt.executeQuery();
	        if (rs.next()) {
	            int count = rs.getInt(1);
	            return count > 0;   // true se ci sono notifiche non lette
	        }
	        return false;
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        return false;
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}
	
	public boolean segnaNotificheColtivatoreComeLette(Coltivatore coltivatore) {
	    Connection conn = null;
	    PreparedStatement stmt = null;

	    try {
	        conn = Connessione.getConnection();

	        String sql = 
	        	    "UPDATE \"notifica\" " +
	        	    "SET \"lettura\" = TRUE " +
	        	    "WHERE \"lettura\" = FALSE " +
	        	    "  AND \"utenti_tag\" LIKE ?";


	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, "%" + coltivatore.getUsername() + "%");

	        int updated = stmt.executeUpdate();
	        return updated > 0;  // true se almeno una notifica è stata aggiornata
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        return false;
	    } finally {
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}
	
	public String getNotificheNonLette(Coltivatore coltivatore) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conn = Connessione.getConnection();

	        String sql =
	        	    "SELECT \"titolo\", \"descrizione\" " +
	        	    "FROM \"notifica\" " +
	        	    "WHERE \"lettura\" = FALSE " +
	        	    "  AND \"utenti_tag\" LIKE ?";


	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, "%" + coltivatore.getUsername() + "%");

	        rs = stmt.executeQuery();

	        StringBuilder sb = new StringBuilder();
	        while (rs.next()) {
	            String titolo = rs.getString("titolo");
	            String descrizione = rs.getString("descrizione");

	            sb.append(titolo).append(" : ").append(descrizione).append("\n");
	        }

	        return sb.toString().trim(); // rimuove eventuale \n finale
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        return "";
	    } finally {
	        try { if (rs != null) rs.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}

	
	public static ArrayList<String> getColtivatoriByProprietario(Proprietario proprietario) { 
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
	                        "JOIN lotto l       ON l.codice_fiscalepr = p.codice_fiscale " +
	                        "JOIN attivita a    ON a.id_lotto = l.id_lotto " +
	                        "JOIN coltivatore c ON c.codice_fiscale = a.codice_fiscalecol " +
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
	    } finally {
	        try { if (risultato != null) risultato.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	    
	    return coltivatori;
	}
	
	
	
}
