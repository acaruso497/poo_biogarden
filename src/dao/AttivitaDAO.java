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
		            sql2 = "SELECT i.id_attivita " +
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
		            sql3 = "SELECT s.id_attivita " +
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
	
		
	public String popolaAttivita(ProgettoColtivazioneDTO progetto, SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta) { //popola il text field di giorno inizio, giorno fine e il radio button dello stato dell'attività
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			String sql1 = null;
			String sql2 = null;
			String sql3 = null;

			try {
				  conn = Connessione.getConnection();

					if (raccolta != null) {
							sql1 = """
							SELECT r.id_attivita, r.stato, r.giorno_inizio, r.giorno_fine
							FROM progetto_coltivazione pc
							JOIN attivita a   ON a.id_progetto = pc.id_progetto
							JOIN raccolta r   ON r.id_attivita = a.id_attivita
							WHERE pc.titolo = ?
							AND (
							r.giorno_inizio BETWEEN pc.data_inizio AND pc.data_fine
							OR r.giorno_fine   BETWEEN pc.data_inizio AND pc.data_fine
							OR pc.data_inizio  BETWEEN r.giorno_inizio AND r.giorno_fine
							)
							ORDER BY r.giorno_inizio DESC, r.giorno_fine DESC
							LIMIT 1
							""";
							stmt = conn.prepareStatement(sql1);
							stmt.setString(1, progetto.getTitolo());
							rs = stmt.executeQuery();
						} else if (irrigazione != null) {
							sql2 = """
							SELECT i.id_attivita, i.stato, i.giorno_inizio, i.giorno_fine
							FROM progetto_coltivazione pc
							JOIN attivita a    ON a.id_progetto = pc.id_progetto
							JOIN irrigazione i ON i.id_attivita = a.id_attivita
							WHERE pc.titolo = ?
							AND (
							i.giorno_inizio BETWEEN pc.data_inizio AND pc.data_fine
							OR i.giorno_fine   BETWEEN pc.data_inizio AND pc.data_fine
							OR pc.data_inizio  BETWEEN i.giorno_inizio AND i.giorno_fine
							)
							ORDER BY i.giorno_inizio DESC, i.giorno_fine DESC
							LIMIT 1
							""";
							stmt = conn.prepareStatement(sql2);
							stmt.setString(1, progetto.getTitolo());
							rs = stmt.executeQuery();
						} else if (semina != null) {
							sql3 = """
						     SELECT s.id_attivita, s.stato, s.giorno_inizio, s.giorno_fine
						     FROM progetto_coltivazione pc
							 JOIN attivita a  ON a.id_progetto = pc.id_progetto
							 JOIN semina s    ON s.id_attivita = a.id_attivita
							 WHERE pc.titolo = ?
							 AND (
							 s.giorno_inizio BETWEEN pc.data_inizio AND pc.data_fine
							 OR s.giorno_fine   BETWEEN pc.data_inizio AND pc.data_fine
							 OR pc.data_inizio  BETWEEN s.giorno_inizio AND s.giorno_fine
							 )
							ORDER BY s.giorno_inizio DESC, s.giorno_fine DESC
							LIMIT 1
							""";
							stmt = conn.prepareStatement(sql3);
							stmt.setString(1, progetto.getTitolo());
							rs = stmt.executeQuery();
							} else {
								return null;
							}
								return rs.getString("stato");
							} catch (SQLException ex) {
								ex.printStackTrace();
								return null;
							} finally {
								try { if (rs != null) rs.close(); } catch (Exception ignored) {}
								try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
								try { if (conn != null) conn.close(); } catch (Exception ignored) {}
							}
			}
		
		
		
	
//	_________________ VISUALIZZA PROGETTI _________________
		
		
	
}
