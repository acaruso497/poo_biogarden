package dao;

import database.Connessione;
import dto.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgettoColtivazioneDAO {

	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________	
	
	
		public static boolean registraProgetto(ProgettoColtivazioneDTO progetto, LottoDTO lotto, ArrayList<ColturaDTO> coltureDTOList) { //Registrazione dei dati del progetto
		    
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet risultato = null;
		    
		    try {
		        conn = Connessione.getConnection();
		        
		        /* controlla se esiste almeno 1 progetto di coltivazione all'interno 
		        di un determinato lotto e se non è stato completato */
		        
		        String sql1 = "SELECT EXISTS (  " +
		        		" SELECT 1 " +
		        	    " FROM Progetto_Coltivazione " +
		        	    " WHERE id_lotto = ? AND done = false" + 
		        	    " ); ";
		        
		        stmt = conn.prepareStatement(sql1);
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
		        String sql2 = "INSERT INTO Progetto_Coltivazione (titolo, descrizione, stima_raccolto, data_inizio, data_fine, id_lotto) "
		        		      + "VALUES (?, ?, ?, ?, ?, ?)";
		        stmt = conn.prepareStatement(sql2);
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
		        if (coltureDTOList != null && coltureDTOList.size() > 0) {
		            for (int i = 0; i < coltureDTOList.size(); i++) {
		                    // Crea o recupera coltura
		                    ColturaDTO coltura = getOrCreateColtura(conn, coltureDTOList.get(i));
		                    
		                    // Associa coltura al lotto
		                    associaColturaALotto(conn, lotto, coltureDTOList.get(i), progetto);
		            }
		        }
		      } //parentesi else 
		        
		     // recupera tutti i coltivatori che lavorano in quel lotto
				List<String> coltivatori = getColtivatoriLotto(lotto);
				if (coltivatori.isEmpty()) {
					return false;
				}
			
				// per ogni coltivatore, viene assegnata un'attività
				for (String coltivatore : coltivatori) {
					String sqlAttivita = "INSERT INTO Attivita (ID_Lotto, Codice_FiscaleCol, giorno_assegnazione, stato, id_progetto) VALUES (?, ?, CURRENT_DATE, 'pianificata', ?)";
					stmt = conn.prepareStatement(sqlAttivita);
					stmt.setInt(1, lotto.getID_Lotto());
					stmt.setString(2, coltivatore);
					stmt.setInt(3, progetto.getID_Progetto());
					risultato = stmt.executeQuery();
			
					int idAttivita = 0;
					if (risultato.next()) {
					idAttivita = risultato.getInt("ID_Attivita");
					}
					risultato.close();
					stmt.close();
			
					
					String sql3 = null;
					
					// inserisce la specifica attività ai coltivatori
					if ("Raccolta".equals(tipoAttivita)) {
						sql3 = "INSERT INTO Raccolta (giorno_inizio, giorno_fine, raccolto_effettivo, id_attivita, stato) VALUES (?, ?, 0, ?, 'pianificata')";
						stmt = conn.prepareStatement(sql3);
						stmt.setDate(1, dataIA);
						stmt.setDate(2, dataFA);
						stmt.setInt(3, idAttivita);
					}
					
					else if ("Semina".equals(tipoAttivita)) {
						sql3 = "INSERT INTO Semina (giorno_inizio, giorno_fine, tipo_semina, profondita, id_attivita, stato) VALUES (?, ?, ?, 10, ?, 'pianificata')";
						stmt = conn.prepareStatement(sql3);
						stmt.setDate(1, dataIA);
						stmt.setDate(2, dataFA);
						stmt.setString(3, tipoSemina);
						stmt.setInt(4, idAttivita);
					}
					
					else if ("Irrigazione".equals(tipoAttivita)) {
						sql3 = "INSERT INTO Irrigazione (giorno_inizio, giorno_fine, tipo_irrigazione, id_attivita, stato) VALUES (?, ?, ?, ?, 'pianificata')";
						stmt = conn.prepareStatement(sql3);
						stmt.setDate(1, dataIA);
						stmt.setDate(2, dataFA);
						stmt.setString(3, tipoIrrigazione);
						stmt.setInt(4, idAttivita);
					}
				
				}
					int rowsAffected = stmt.executeUpdate();
		        
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
		
		// crea la coltura dalla varietà
		private static ColturaDTO getOrCreateColtura(Connection conn, ColturaDTO coltura) throws SQLException {
		    String selectSql = "SELECT id_coltura FROM Coltura WHERE varietà = ?";
		    PreparedStatement stmt = conn.prepareStatement(selectSql);
		    stmt.setString(1, coltura.getVarieta());
		    ResultSet rs = stmt.executeQuery();
		    
		    if (rs.next()) {
		    	coltura.setID_Coltura(rs.getInt("id_coltura"));
		        rs.close();
		        stmt.close();
		        return coltura;
		    }
		    rs.close();
		    stmt.close();
		    
		    
		    String insertSql = "INSERT INTO Coltura (varietà) VALUES (?)";
		    stmt = conn.prepareStatement(insertSql);
		    stmt.setString(1, coltura.getVarieta());
		    int rows = stmt.executeUpdate();
		    stmt.close();
		    
		    if (rows > 0) {
		        return coltura;
		    }
		    
		    throw new SQLException("Impossibile creare coltura: " + coltura.getVarieta());
		}
		
		
		// associa la coltura al lotto
		private static void associaColturaALotto(Connection conn, LottoDTO lotto, ColturaDTO coltura, ProgettoColtivazioneDTO progetto) throws SQLException {
			
			String checkSql = "SELECT 1 FROM Progetto_Coltura WHERE id_progetto = ? AND id_coltura = ?";
		    PreparedStatement checkStmt = conn.prepareStatement(checkSql);
		    checkStmt.setInt(1, progetto.getID_Progetto());
		    checkStmt.setInt(2, coltura.getID_Coltura());
		    ResultSet rs = checkStmt.executeQuery();
		    
		    if (!rs.next()) {  // Inserisci solo se non esiste
		        String sql = "INSERT INTO Progetto_Coltura (id_progetto, id_coltura) VALUES (?, ?)";
		        PreparedStatement stmt = conn.prepareStatement(sql);
		        stmt.setInt(1, progetto.getID_Progetto());
		        stmt.setInt(2, coltura.getID_Coltura());
		        stmt.executeUpdate();
		        stmt.close();
		    }
		    
		    rs.close();
		    checkStmt.close();
			
		}
		
		//Recupera il coltivatore dal lotto
		private static List<String> getColtivatoriLotto(LottoDTO lotto) {
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			List<String> coltivatori = new ArrayList<>();
			
			try {
				conn = Connessione.getConnection();
				String sql = "SELECT DISTINCT a.Codice_FiscaleCol FROM Attivita a WHERE a.ID_Lotto = ? ";;

				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, lotto.getID_Lotto());
				rs = stmt.executeQuery();
			
			while (rs.next()) {
				coltivatori.add(rs.getString("Codice_FiscaleCol"));
			}
				return coltivatori; 
			
			} catch (SQLException ex) {
				ex.printStackTrace();
				return null;
			} finally {
				try { if (rs != null) rs.close(); } catch (Exception e) {}
				try { if (stmt != null) stmt.close(); } catch (Exception e) {}
				try { if (conn != null) conn.close(); } catch (Exception e) {}
			}
		}	
		
		
		
		
	
}
