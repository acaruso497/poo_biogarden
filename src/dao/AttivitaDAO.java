package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class AttivitaDAO {

//	_________________ VISUALIZZA PROGETTI _________________
	
	// Aggiorna lo stato di ciascuna attività
		public boolean aggiornaStato(SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta, LottoDTO lotto) {
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet risultato = null;
		    String sql1 = null;
		    String sql2 = null;
		    String sql3 = null;
		    String sql4 = null;
		    String sql5 = null;
		    String sql6=null;
		    
		    try {
		        conn = Connessione.getConnection();
		        
		        // seleziona l'ID attività più recente per il lotto e tipo di attività
		        if (raccolta!=null) {
		            sql1 = "SELECT r.id_attivita " +
		                   "FROM Raccolta r " +
		                   "JOIN Attivita a ON r.id_attivita = a.id_attivita " +
		                   "WHERE a.id_lotto = ? " +
		                   "ORDER BY r.giorno_inizio DESC, r.giorno_fine DESC " +
		                   "LIMIT 1";
		            
		            stmt = conn.prepareStatement(sql1);
		            stmt.setInt(1, lotto.getID_Lotto());
		            risultato = stmt.executeQuery();

		            if (risultato.next()) {
		                raccolta.setID_Attivita(risultato.getInt("id_attivita"));
		            } else return false;

		            risultato.close();
		            stmt.close();

		            sql2 = "UPDATE Raccolta SET stato = ? WHERE id_attivita = ?";
		            stmt = conn.prepareStatement(sql2);
		            stmt.setString(1, raccolta.getStato());
		            stmt.setInt(2, raccolta.getID_Attivita());
		            return stmt.executeUpdate() > 0;
		            
		            
		        } else if (irrigazione!=null) {
		            sql3 = "SELECT i.id_attivita " +
		                   "FROM Irrigazione i " +
		                   "JOIN Attivita a ON i.id_attivita = a.id_attivita " +
		                   "WHERE a.id_lotto = ? " +
		                   "ORDER BY i.giorno_inizio DESC, i.giorno_fine DESC " +
		                   "LIMIT 1";
		            
		            stmt = conn.prepareStatement(sql3);
		            stmt.setInt(1, lotto.getID_Lotto());
		            risultato = stmt.executeQuery();

		            if (risultato.next()) {
		                irrigazione.setID_Attivita(risultato.getInt("id_attivita"));
		            } else return false;

		            risultato.close();
		            stmt.close();

		            sql4 = "UPDATE Irrigazione SET stato = ? WHERE id_attivita = ?";
		            stmt = conn.prepareStatement(sql4);
		            stmt.setString(1, irrigazione.getStato());
		            stmt.setInt(2, irrigazione.getID_Attivita());
		            return stmt.executeUpdate() > 0;
		            
		        } else if (semina!=null) {
		            sql5 = "SELECT s.id_attivita " +
		                   "FROM Semina s " +
		                   "JOIN Attivita a ON s.id_attivita = a.id_attivita " +
		                   "WHERE a.id_lotto = ? " +
		                   "ORDER BY s.giorno_inizio DESC, s.giorno_fine DESC " +
		                   "LIMIT 1";
		            
		            stmt = conn.prepareStatement(sql5);
		            stmt.setInt(1, lotto.getID_Lotto());
		            risultato = stmt.executeQuery();

		            if (risultato.next()) {
		                semina.setID_Attivita(risultato.getInt("id_attivita"));
		            } else return false;

		            risultato.close();
		            stmt.close();

		            sql6 = "UPDATE Semina SET stato = ? WHERE id_attivita = ?";
		            stmt = conn.prepareStatement(sql6);
		            stmt.setString(1, semina.getStato());
		            stmt.setInt(2, semina.getID_Attivita());
		            return stmt.executeUpdate() > 0;
		        }
		       return false;
		        
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        return false; 
		    } finally {
		        try { if (risultato != null) risultato.close(); } catch (Exception e) {}
		        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
		        try { if (conn != null) conn.close(); } catch (Exception e) {}
		    }
		}
	
		
		
	
//	_________________ VISUALIZZA PROGETTI _________________
		
		
	
}
