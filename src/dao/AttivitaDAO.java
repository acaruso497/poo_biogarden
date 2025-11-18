package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class AttivitaDAO implements IAttivitaDAO{

//	_________________ VISUALIZZA PROGETTI _________________
		public boolean aggiornaStato(SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta, LottoDTO lotto) { // Aggiorna lo stato di ciascuna attività
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet risultato = null;
		    String sql1 = null;
		    String sql3 = null;
		    String sql5 = null;
		    boolean aggiornata = false;
		    
		    try {
		        conn = Connessione.getConnection();
		        
		        if (raccolta!=null) {
		            sql1 = "SELECT r.id_attivita " +
		                   "FROM Raccolta r " +
		                   "JOIN Attivita a ON r.id_attivita = a.id_attivita " +
		                   "WHERE a.id_lotto = ? " +
		                   "ORDER BY r.giorno_inizio DESC, r.giorno_fine DESC ";
		            
		            stmt = conn.prepareStatement(sql1);
		            stmt.setInt(1, lotto.getID_Lotto());
		            risultato = stmt.executeQuery();
		            
		            aggiornata = false;
		            
		            while (risultato.next()) { // MODIFICA: while per tutte le righe
		                int idAtt = risultato.getInt("id_attivita");
		                
		                String sqlUpdate = "UPDATE Raccolta SET stato = ? WHERE id_attivita = ?";
		                PreparedStatement stmtUpd = conn.prepareStatement(sqlUpdate);
		                stmtUpd.setString(1, raccolta.getStato());
		                stmtUpd.setInt(2, idAtt);
		                if (stmtUpd.executeUpdate() > 0) aggiornata = true; // MODIFICA: aggiorna flag
		                stmtUpd.close();
		            }
		            

		            risultato.close();
		            stmt.close();
		            
		            if (!aggiornata) return false;
		        } 
		        
		        
		        if (irrigazione!=null) {
		            sql3 = "SELECT i.id_attivita " +
		                   "FROM Irrigazione i " +
		                   "JOIN Attivita a ON i.id_attivita = a.id_attivita " +
		                   "WHERE a.id_lotto = ? " +
		                   "ORDER BY i.giorno_inizio DESC, i.giorno_fine DESC ";
		            
		            stmt = conn.prepareStatement(sql3);
		            stmt.setInt(1, lotto.getID_Lotto());
		            risultato = stmt.executeQuery();
		            
		            aggiornata = false;

		            while (risultato.next()) {
		                int idAtt = risultato.getInt("id_attivita");

		                String sqlUpdate = "UPDATE Irrigazione SET stato = ? WHERE id_attivita = ?";
		                PreparedStatement stmtUpd = conn.prepareStatement(sqlUpdate);
		                stmtUpd.setString(1, irrigazione.getStato());
		                stmtUpd.setInt(2, idAtt);
		                if (stmtUpd.executeUpdate() > 0) aggiornata = true;
		                stmtUpd.close();
		            }
		            

		            risultato.close();
		            stmt.close();
		            
		            if (!aggiornata) return false;
		            
		        } 
		        
		        if (semina!=null) {
		            sql5 = "SELECT s.id_attivita " +
		                   "FROM Semina s " +
		                   "JOIN Attivita a ON s.id_attivita = a.id_attivita " +
		                   "WHERE a.id_lotto = ? " +
		                   "ORDER BY s.giorno_inizio DESC, s.giorno_fine DESC ";
		            
		            stmt = conn.prepareStatement(sql5);
		            stmt.setInt(1, lotto.getID_Lotto());
		            risultato = stmt.executeQuery();
		            
		            aggiornata = false;

		            while (risultato.next()) {
		                int idAtt = risultato.getInt("id_attivita");

		                String sqlUpdate = "UPDATE Semina SET stato = ? WHERE id_attivita = ?";
		                PreparedStatement stmtUpd = conn.prepareStatement(sqlUpdate);
		                stmtUpd.setString(1, semina.getStato());
		                stmtUpd.setInt(2, idAtt);
		                if (stmtUpd.executeUpdate() > 0) aggiornata = true;
		                stmtUpd.close();
		            }
		            

		            risultato.close();
		            stmt.close();
		            
		            if (!aggiornata) return false;

		        }
		       return true;
		        
		    } catch (SQLException ex) {
		        ex.printStackTrace();
		        return false; 
		    } 
		}	
//	_________________ VISUALIZZA PROGETTI _________________	
}
