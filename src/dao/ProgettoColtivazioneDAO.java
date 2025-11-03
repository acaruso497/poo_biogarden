package dao;

import database.Connessione;
import dto.*;
import java.sql.*;

public class ProgettoColtivazioneDAO {

	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________	
	
	
		public static boolean registraProgetto(ProgettoColtivazioneDTO progetto, String[] coltureArray) { //Registrazione dei dati del progetto
		    
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet risultato = null;
		    
		    try {
		        conn = Connessione.getConnection();
		        
		        /* controlla se esiste almeno 1 progetto di coltivazione all'interno 
		        di un determinato lotto e se non è stato completato */
		        
		        String sql = "SELECT EXISTS (  " +
		        		" SELECT 1 " +
		        	    " FROM Progetto_Coltivazione " +
		        	    " WHERE id_lotto = ? AND done = false" + 
		        	    " ); ";
		        
		        stmt = conn.prepareStatement(sql);
		        stmt.setInt(1, progetto.getIdLotto());
		        risultato = stmt.executeQuery();
		        
		        boolean esiste = false;
		        if(risultato.next()) {
		        	esiste = risultato.getBoolean("exists");
		        }
		        risultato.close();
		        stmt.close();
		        
		        if(esiste==true) {
		        	System.out.println("Esiste già un progetto in questo lotto!");
		        	 return false;
		        }else {
		        	
		        //inserisce tutte le informazioni dei textfield dentro progetto coltivazione
		        String sql1 = "INSERT INTO Progetto_Coltivazione (titolo, descrizione, stima_raccolto, data_inizio, data_fine, id_lotto) "
		        		      + "VALUES (?, ?, ?, ?, ?, ?) RETURNING id_progetto";
		        stmt = conn.prepareStatement(sql1);
		        stmt.setString(1, progetto.getTitolo());
		        stmt.setString(2, progetto.getDescrizione());
		        stmt.setDouble(3, progetto.getStimaRaccolto());
		        stmt.setDate(4, progetto.getDataInizio());
		        stmt.setDate(5, progetto.getDataFine());
		        stmt.setInt(6, progetto.getIdLotto());
		        
		        risultato = stmt.executeQuery();
		        
		        
		        risultato.close();
		        stmt.close();
		        
		        //pulisce le colture dalle virgole
		        if (coltureArray != null && coltureArray.length > 0) {
		            for (int i = 0; i < coltureArray.length; i++) {
		                String coltura = coltureArray[i];
		                String colturaPulita = coltura.trim();
		                
		                if (!colturaPulita.isEmpty()) {
		                    // Crea o recupera coltura
		                    int colturaId = getOrCreateColtura(conn, colturaPulita);
		                    
		                    // Associa coltura al lotto
		                    associaColturaALotto(conn, progetto.getIdLotto(), colturaId, progetto.getID_Progetto());
		                }
		            }
		        }
		      }  
		        
		    return true;
		    } catch(SQLException | NumberFormatException ex) {
		        ex.printStackTrace();
		        return false;
		    } finally {
		        try { if (risultato != null) risultato.close(); } catch (Exception e) {}
		        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
		        try { if (conn != null) conn.close(); } catch (Exception e) {}
		    }
		}
		
		
		
	
}
