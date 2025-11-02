package dao;

import java.sql.PreparedStatement;

import database.Connessione;
import dto.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgettoColtivazioneD {

	// controlla se esiste la coltura già su un lotto
		public static boolean checkColtura(Lotto lotto, String[] coltureArray) {
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
		
		//Registrazione dei dati del progetto
		public static boolean registraProgetto(ProgettoColtivazione progetto, String[] coltureArray, AtomicInteger idProgettoOut) {
		    //int idLotto = Integer.parseInt(idLottoStr);  //converte l'ID del lotto nella combo box in un intero
		    
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
		        
		        int idProgetto = 0;
		        if (risultato.next()) {
		            idProgetto = risultato.getInt("id_progetto");
		            idProgettoOut.set(idProgetto);
		        }
		        
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
