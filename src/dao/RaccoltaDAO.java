package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class RaccoltaDAO {
				//	_________________ HOMEPAGE COLTIVATORE  _________________

	// da vedeere se serve progetto coltivazione dto da chiedere 
	
	public static boolean sommaRaccolto(String raccolto,  ColturaDTO coltura, ProgettoColtivazioneDTO progetto) {
	    Connection conn = null;
	    PreparedStatement ps = null;
	    try {
	        int nuovo = Integer.parseInt(raccolto);

	        conn = Connessione.getConnection();
	        conn.setAutoCommit(false);

	        String sql =
	            "UPDATE Coltura " +
	            "SET " +
	            "  raccoltoprodotto = raccoltoprodotto + ?," +
	            "  max = GREATEST(max, ?)," +
	            "  min = CASE WHEN min = 0 OR ? < min THEN ? ELSE min END," +
	            "  counter = counter + 1," +
	            "  avg = (raccoltoprodotto + ?) / (counter + 1) " +  
	            "WHERE varietà = ?";

	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, nuovo);
	        ps.setInt(2, nuovo);
	        ps.setInt(3, nuovo);
	        ps.setInt(4, nuovo);
	        ps.setInt(5, nuovo);
	        ps.setString(6, coltura.getVarieta());

	        int rows = ps.executeUpdate();
	        conn.commit();
	        return rows > 0;

	    } catch (Exception e) {
	        try { if (conn != null) conn.rollback(); } catch (Exception ignore) {}
	        e.printStackTrace();
	        return false;
	    } finally {
	        try { if (ps != null) ps.close(); } catch (Exception ignore) {}
	        try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ignore) {}
	        try { if (conn != null) conn.close(); } catch (Exception ignore) {}
	    }
	}
	
			//	_________________ HOMEPAGE COLTIVATORE  _________________
	
			//	_________________ VISUALIZZA PROGETTI _________________
	
	public static void popolaRaccolta(ProgettoColtivazioneDTO progetto, RaccoltaDTO raccolta) { //popola il text field di giorno inizio, giorno fine e il radio button dello stato dell'attività
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;

		try {
			  conn = Connessione.getConnection();

				if (raccolta != null) {
						sql = """
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
						stmt = conn.prepareStatement(sql);
						stmt.setString(1, progetto.getTitolo());
						
						rs = stmt.executeQuery();
						
						if (rs.next()) { 
							raccolta.setStato(rs.getString("stato"));
							raccolta.setGiornoInizio(rs.getDate("giorno_inizio"));
							raccolta.setGiornoFine(rs.getDate("giorno_fine"));
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
