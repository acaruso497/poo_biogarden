package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class LottoDAO {

	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________
	
//	public boolean checkColtura(LottoDTO lotto, String[] coltureArray) { // controlla se esiste la coltura già su un lotto
//		String sql = "SELECT 1 FROM controllocolture " +
//				              "WHERE id_lotto = ? AND varietà = ANY(?) " +
//				              "LIMIT 1";
//			    
//		try (Connection conn = Connessione.getConnection();
//			 PreparedStatement stmt = conn.prepareStatement(sql)) {
//			        
//			  stmt.setInt(1, lotto.getID_Lotto());
//			  stmt.setArray(2, conn.createArrayOf("VARCHAR", coltureArray));
//			        
//			   ResultSet rs = stmt.executeQuery();
//			   return rs.next(); // true se trova ALMENO una corrispondenza
//			        
//			 } catch (Exception e) {
//			        e.printStackTrace();
//			        return false;
//			  }
//			}
	
	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________
	
	
	//  _________________ VISUALIZZA PROGETTI _________________
	public String getLottoProgettoByProprietario(ProgettoColtivazioneDTO progetto, ProprietarioDTO proprietario) { //recupera i lotti di un proprietario (utile per popolare ComboLotti)
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet risultato = null;
	    String idLotto = null;

	    try {
	        conn = Connessione.getConnection(); 
	        String sql = "SELECT l.ID_Lotto " +
	                     "FROM Lotto l " +
	                     "JOIN Progetto_Coltivazione pc ON l.ID_Lotto = pc.ID_Lotto " +  
	                     "WHERE pc.titolo = ? " +
	                     "AND l.Codice_FiscalePr = ?";
	     
	        stmt = conn.prepareStatement(sql);   
	        stmt.setString(1, progetto.getTitolo()); 
	        stmt.setString(2, proprietario.getCodiceFiscale());
	        risultato = stmt.executeQuery();

	        if (risultato.next()) {
	        	idLotto = String.valueOf(risultato.getInt("ID_Lotto"));
	        }

	    } catch (SQLException ex) {
	    	ex.printStackTrace();
	    } finally {
	        try { if (risultato != null) risultato.close(); } catch (Exception ignored) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
	        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
	    }
	    return idLotto;
	}
	
//  _________________ VISUALIZZA PROGETTI _________________
	
}
