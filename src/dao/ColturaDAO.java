package dao;

import database.Connessione;
import dto.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
		
		
		
//  _________________ GRAFICO _________________
		
		//recupera la varietà della coltura (utile per popolare ComboColtura)
		public static List<String> getColturaByLotto(LottoDTO lotto) {
				    List<String> lista = new ArrayList<>(); 
				    Connection conn = null;
				    PreparedStatement stmt = null;
				    ResultSet risultato = null;

				    try {
				        conn = Connessione.getConnection(); 

				        String sql = "SELECT varietà FROM ProprietarioRaccoltoColture WHERE id_lotto = ?";  //seleziona le informazioni della coltura dalla view
				    
				        stmt = conn.prepareStatement(sql);   
				        stmt.setInt(1, lotto.getID_Lotto());         
				        risultato = stmt.executeQuery();

				        while (risultato.next()) {
				            String varietà = risultato.getString("varietà"); 
			                lista.add(varietà); // Aggiunge la varietà alla lista
				        }

				    } catch (SQLException ex) {
				    	ex.printStackTrace();
				    } finally {
				        try { if (risultato != null) risultato.close(); } catch (Exception ignored) {}
				        try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
				        try { if (conn != null) conn.close(); } catch (Exception ignored) {}
				    }
				    return lista;
		}		
		
		// === Letture dagli aggregati già salvati in Coltura ===

		public static long getNumeroRaccolte(ColturaDTO coltura) {
		    Number n = queryColturaVal("counter", coltura.getVarieta());
		    return n != null ? n.longValue() : 0L;
		}

		public static double getMediaRaccolto(ColturaDTO coltura) {
		    Number n = queryColturaVal("avg", coltura.getVarieta());
		    return n != null ? n.doubleValue() : 0.0;
		}

		public static double getMinRaccolto(ColturaDTO coltura) {
		    Number n = queryColturaVal("min", coltura.getVarieta());
		    return n != null ? n.doubleValue() : 0.0;
		}

		public static double getMaxRaccolto(ColturaDTO coltura) {
		    Number n = queryColturaVal("max", coltura.getVarieta());
		    return n != null ? n.doubleValue() : 0.0;
		}

		// ===== Helper unico per leggere una colonna dalla tabella Coltura =====
		private static Number queryColturaVal(String column, String varieta) {
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet rs = null;
		    try {
		        conn = Connessione.getConnection();

		        String sql =
		            "SELECT " + column + " AS val " +
		            "FROM Coltura " +
		            "WHERE lower(trim(\"varietà\")) = lower(trim(?)) " +
		            "LIMIT 1";

		        stmt = conn.prepareStatement(sql);
		        stmt.setString(1, varieta);

		        rs = stmt.executeQuery();
		        if (rs.next()) {
		            Object o = rs.getObject("val");
		            if (o == null) return 0;
		            if (o instanceof Number) return (Number) o;
		            return Double.valueOf(o.toString());
		        }
		        return 0;
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        return 0;
		    } finally {
		        try { if (rs != null) rs.close(); } catch (Exception ignore) {}
		        try { if (stmt != null) stmt.close(); } catch (Exception ignore) {}
		        try { if (conn != null) conn.close(); } catch (Exception ignore) {}
		    }
		}
		
//  _________________ GRAFICO _________________
	
	
}
