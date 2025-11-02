package dao;

import database.Connessione;
import utils.method;
import dto.*;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class ColtivatoreD {
	//____________________   REGISTRAZIONE     ____________________________________
		
	public static boolean registraC(Coltivatore coltivatore) {		// Registrazione COLTIVATORE
			    Connection conn = null;
			    PreparedStatement stmt = null;
			    try {
			        conn = Connessione.getConnection();

			        String sql = """
			            INSERT INTO Coltivatore 
			            (Codice_Fiscale, nome, cognome, username, psw, esperienza, username_proprietario)
			            VALUES (?, ?, ?, ?, ?, 'principiante', ?)
			        """;

			        stmt = conn.prepareStatement(sql);
			        stmt.setString(1, coltivatore.getCodiceFiscale());
			        stmt.setString(2, coltivatore.getNome());
			        stmt.setString(3, coltivatore.getCognome());
			        stmt.setString(4, coltivatore.getUsername());
			        stmt.setString(5, coltivatore.getPassword());
			        stmt.setString(6, coltivatore.getUsernameProprietario());

			        int rows = stmt.executeUpdate(); //esegue l'INSERT e restituisce la riga inserita
			        System.out.println("REGISTRAZIONE AVVENUTA CON SUCCESSO");
			        return rows == 1; //se è stata inserita una riga, ritorna true
			        

			    } catch (SQLException ex) {
			        ex.printStackTrace();
			        return false;
			    } finally {
			        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
			        try { if (conn != null) conn.close(); } catch (Exception e) {}
			    }
			}
			//____________________   REGISTRAZIONE     ____________________________________
			
}
