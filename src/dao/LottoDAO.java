package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class LottoDAO implements ILottoDAO{


	
	
	//  _________________ VISUALIZZA PROGETTI _________________
	public String getLottoProgettoByProprietario(ProgettoColtivazioneDTO progetto, ProprietarioDTO proprietario) { //recupera i lotti di un proprietario (utile per popolare ComboLotti)
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet risultato = null;
	    String idLotto = null;

	    try {
	        conn = Connessione.getConnection(); 
	        String sql = "SELECT l.ID_Lotto " +
	                     "FROM Lotto l " +
	                     "JOIN Progetto_Coltivazione pc ON l.ID_Lotto = pc.ID_Lotto " +  
	                     "WHERE pc.titolo = ? " +
	                     "AND l.Codice_FiscalePr = ?";
	     
	        stmt = conn.prepareStatement(sql);   
	        stmt.setString(1, progetto.getTitolo()); 
	        stmt.setString(2, proprietario.getCodiceFiscale());
	        risultato = stmt.executeQuery();

	        if (risultato.next()) {
	        	idLotto = String.valueOf(risultato.getInt("ID_Lotto"));
	        }

	    } catch (SQLException ex) {
	    	ex.printStackTrace();
	    }
	    return idLotto;
	}
	
//  _________________ VISUALIZZA PROGETTI _________________
	
}
