package dao;

import database.Connessione;
import dto.*;
import java.sql.*;


public class SeminaDAO {
						//	_________________ VISUALIZZA PROGETTI _________________
	
	public static void popolaSemina(ProgettoColtivazioneDTO progetto, SeminaDTO semina) { //popola il text field di giorno inizio, giorno fine e il radio button dello stato dell'attività
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;

		try {
			  conn = Connessione.getConnection();

				if (semina != null) {
						sql = """
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
						stmt = conn.prepareStatement(sql);
						stmt.setString(1, progetto.getTitolo());
						
						rs = stmt.executeQuery();
						
						if (rs.next()) { 
							 semina.setStato(rs.getString("stato"));
							 semina.setGiornoInizio(rs.getDate("giorno_inizio"));
							 semina.setGiornoFine(rs.getDate("giorno_fine"));
						 }
						
					     }
				} catch (SQLException ex) {
					ex.printStackTrace();
					} finally {
					  try { if (rs != null) rs.close(); } catch (Exception ignored) {}
					  try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
					  try { if (conn != null) conn.close(); } catch (Exception ignored) {}
				}
		}
	
			//	_________________ VISUALIZZA PROGETTI _________________
	
	
}
