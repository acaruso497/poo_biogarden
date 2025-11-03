package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class LottoDAO {

	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________
	
	// controlla se esiste la coltura già su un lotto
	public static boolean checkColtura(LottoDTO lotto, String[] coltureArray) {
		String sql = "SELECT 1 FROM controllocolture " +
				              "WHERE id_lotto = ? AND varietà = ANY(?) " +
				              "LIMIT 1";
			    
		try (Connection conn = Connessione.getConnection();
			 PreparedStatement stmt = conn.prepareStatement(sql)) {
			        
			  stmt.setInt(1, lotto.getID_Lotto());
			  stmt.setArray(2, conn.createArrayOf("VARCHAR", coltureArray));
			        
			   ResultSet rs = stmt.executeQuery();
			   return rs.next(); // true se trova ALMENO una corrispondenza
			        
			 } catch (Exception e) {
			        e.printStackTrace();
			        return false;
			  }
			}
	
	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________
	
}
