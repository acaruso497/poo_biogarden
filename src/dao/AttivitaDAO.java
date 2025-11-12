package dao;
import database.Connessione;
import dto.*;
import java.sql.*;

public class AttivitaDAO {

	//popola il text field di giorno inizio, giorno fine e il radio button dello stato dell'attività
		public String popolaAttivita(ProgettoColtivazioneDTO progetto, SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta) {
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
		
		
	
}
