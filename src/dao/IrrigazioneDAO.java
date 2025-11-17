package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class IrrigazioneDAO {

	//	_________________ VISUALIZZA PROGETTI _________________
	public void popolaIrrigazione(ProgettoColtivazioneDTO progetto, IrrigazioneDTO irrigazione) { //popola il text field di giorno inizio, giorno fine e il radio button dello stato dell'attività
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String sql = null;

		try {
			  conn = Connessione.getConnection();

				if (irrigazione != null) {
						sql = """
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
						stmt = conn.prepareStatement(sql);
						stmt.setString(1, progetto.getTitolo());
						
						rs = stmt.executeQuery();
						
						if (rs.next()) { 
							irrigazione.setStato(rs.getString("stato"));
							irrigazione.setGiornoInizio(rs.getDate("giorno_inizio"));
							irrigazione.setGiornoFine(rs.getDate("giorno_fine"));
						}
						
					     }
				} catch (SQLException ex) {
					ex.printStackTrace();
					} 
		}
	
			//	_________________ VISUALIZZA PROGETTI _________________
	
}
