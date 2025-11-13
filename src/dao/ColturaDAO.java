package dao;

import database.Connessione;
import dto.*;
import java.sql.*;
import java.util.ArrayList;

public class ColturaDAO {

//  				_________________ VISUALIZZA PROGETTI _________________
	
	public String getRaccoltoProdotto(ProprietarioDTO proprietario, LottoDTO lotto, ArrayList<ColturaDTO> coltureList) { //restituisce il raccolto prodotto dalla coltura	
	    String raccolto = "";
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet risultato = null;
	    
	    try {
	        conn = Connessione.getConnection();
	        String sql = "SELECT c.raccoltoProdotto " +
	                     "FROM Coltura c " +
	                     "JOIN Progetto_Coltura pc ON c.ID_Coltura = pc.ID_Coltura " +
	                     "JOIN Progetto_Coltivazione pcol ON pc.ID_Progetto = pcol.ID_Progetto " +
	                     "JOIN Lotto l ON pcol.ID_Lotto = l.ID_Lotto " +
	                     "JOIN Proprietario p ON l.Codice_FiscalePr = p.Codice_Fiscale " +
	                     "WHERE p.username = ? AND l.ID_Lotto = ? AND c.varietà = ?";
	        
	        stmt = conn.prepareStatement(sql);
	        for (ColturaDTO coltura : coltureList) {
	        stmt.setString(1, proprietario.getUsername());
	        stmt.setInt(2, lotto.getID_Lotto());
	        stmt.setString(3, coltura.getVarieta());
	        
	        risultato = stmt.executeQuery();
	        
	        if (risultato.next()) {
	            raccolto = risultato.getString("raccoltoProdotto");
	            if (raccolto != null) {
	                raccolto += " kg"; // Aggiungi l'unità per uniformità
	            }
	        }
	     }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    } finally {
	        try { if (risultato != null) risultato.close(); } catch (Exception ignored) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
	        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
	    }
	    return raccolto.isEmpty() ? "nessun dato!!!" : raccolto; // Restituisci "0 kg" se non trovato
	}
	
	
	
		public ArrayList<String> getColtureProprietario(ProprietarioDTO proprietario, ProgettoColtivazioneDTO progetto) { //restituisce le colture presenti nel lotto del progetto di coltivazione in riferimento al proprietario (utile per la dropdown)
		    ArrayList<String> listaC = new ArrayList<>();
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet risultato = null;
		    
		    try {
		        conn = Connessione.getConnection();
		        String sql = "SELECT DISTINCT c.varietà " +
		                     "FROM Coltura c " +
		                     "JOIN Progetto_Coltura pc ON c.ID_Coltura = pc.ID_Coltura " +
		                     "JOIN Progetto_Coltivazione pcol ON pc.ID_Progetto = pcol.ID_Progetto " +
		                     "JOIN Lotto l ON pcol.ID_Lotto = l.ID_Lotto " +
		                     "JOIN Proprietario p ON l.Codice_FiscalePr = p.Codice_Fiscale " +
		                     "WHERE p.codice_fiscale = ? AND pcol.titolo = ?";
		        
		        stmt = conn.prepareStatement(sql);
		        stmt.setString(1, proprietario.getCodiceFiscale());
		        stmt.setString(2, progetto.getTitolo());
		        risultato = stmt.executeQuery();
		        
		        while (risultato.next()) {
		            listaC.add(risultato.getString("varietà"));
		        }
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		    } finally {
		        try { if (risultato != null) risultato.close(); } catch (Exception ignored) {}
		        try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
		        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
		    }
		    return listaC;
		}
	
	
//	_________________ VISUALIZZA PROGETTI _________________
	
	
}
