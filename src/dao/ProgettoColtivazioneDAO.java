
package dao;

import database.Connessione;
import dto.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProgettoColtivazioneDAO {

	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________	
		public boolean registraProgetto(ProgettoColtivazioneDTO progetto, LottoDTO lotto, ArrayList<ColturaDTO> coltureDTOList) { //Registrazione dei dati del progetto
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet risultato = null;
		    
		    try {
		        conn = Connessione.getConnection();
		        
		        //inserisce tutte le informazioni dei textfield dentro progetto coltivazione
		        String sql2 = "INSERT INTO Progetto_Coltivazione (titolo, descrizione, stima_raccolto, data_inizio, data_fine, id_lotto) "
		        		      + "VALUES (?, ?, ?, ?, ?, ?) RETURNING ID_Progetto";
		        stmt = conn.prepareStatement(sql2);
		        stmt.setString(1, progetto.getTitolo());
		        stmt.setString(2, progetto.getDescrizione());
		        stmt.setDouble(3, progetto.getStimaRaccolto());
		        stmt.setDate(4, progetto.getDataInizio());
		        stmt.setDate(5, progetto.getDataFine());
		        stmt.setInt(6, progetto.getIdLotto());
		        
		        risultato = stmt.executeQuery();

		        if (!risultato.next()) {
		            throw new SQLException("Impossibile recuperare l'ID del progetto creato");
		        }
		        
		        int idProgetto = risultato.getInt("ID_Progetto");  
		        progetto.setID_Progetto(idProgetto);

		        risultato.close();
		        stmt.close();
		        
		       
		        if (coltureDTOList != null && coltureDTOList.size() > 0) {  //pulisce le colture dalle virgole
		            for (int i = 0; i < coltureDTOList.size(); i++) {
		                    
		                    ColturaDTO coltura = getOrCreateColtura(conn, coltureDTOList.get(i)); // Crea o recupera coltura
		                    
		                    associaColturaALotto(conn, lotto, coltura, progetto); // Associa coltura al lotto
		            }
		        }
					return true;
		    } catch(SQLException | NumberFormatException ex) {
		        ex.printStackTrace();
		        return false;
		    } 
		}
		
		public boolean insertAttivita(SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta, LottoDTO lotto, ProgettoColtivazioneDTO progetto) {
			Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet risultato = null;
		    
		    try {
		    	conn = Connessione.getConnection();
		    	String sql3 = null;
				String sql4 = null;
				String sql5 = null;
				
				ArrayList<ColtivatoreDTO> coltivatori = getColtivatoriLotto(lotto); // recupera tutti i coltivatori che lavorano in quel lotto 
				if (coltivatori.isEmpty()) {
					return false;
				}
			
				for (ColtivatoreDTO coltivatore : coltivatori) { // per ogni coltivatore, viene assegnata un'attività
						AttivitaDTO attivita = new AttivitaDTO();
						String sqlAttivita = "INSERT INTO Attivita (ID_Lotto, Codice_FiscaleCol, giorno_assegnazione, stato, id_progetto) VALUES (?, ?, CURRENT_DATE, 'pianificata', ?) RETURNING ID_Attivita";
						stmt = conn.prepareStatement(sqlAttivita);
						stmt.setInt(1, lotto.getID_Lotto());
						stmt.setString(2, coltivatore.getCodiceFiscale());
						stmt.setInt(3, progetto.getID_Progetto());
						risultato = stmt.executeQuery();
				
						if (risultato.next()) {
							attivita.setID_Attivita(risultato.getInt("ID_Attivita"));
							attivita.setCodiceFiscaleCol(coltivatore.getCodiceFiscale());
						}
						risultato.close();
						stmt.close();
						
						// inserisce la specifica attività ai coltivatori
						if(raccolta!=null) {
							sql3 = "INSERT INTO Raccolta (giorno_inizio, giorno_fine, raccolto_effettivo, id_attivita, stato) VALUES (?, ?, 0, ?, 'pianificata')";
							stmt = conn.prepareStatement(sql3);
							stmt.setDate(1, raccolta.getGiornoInizio());
							stmt.setDate(2, raccolta.getGiornoFine());
							stmt.setInt(3, attivita.getID_Attivita());
							stmt.executeUpdate();
						}
						
						
						if(semina!=null) {
							sql4 = "INSERT INTO Semina (giorno_inizio, giorno_fine, tipo_semina, profondita, id_attivita, stato) VALUES (?, ?, ?, 10, ?, 'pianificata')";
							stmt = conn.prepareStatement(sql4);
							stmt.setDate(1, semina.getGiornoInizio());
							stmt.setDate(2, semina.getGiornoFine());
							stmt.setString(3, semina.getTipoSemina());
							stmt.setInt(4, attivita.getID_Attivita());
							stmt.executeUpdate();
						}
						
						
						if(irrigazione!=null) {
							sql5 = "INSERT INTO Irrigazione (giorno_inizio, giorno_fine, tipo_irrigazione, id_attivita, stato) VALUES (?, ?, ?, ?, 'pianificata')";
							stmt = conn.prepareStatement(sql5);
							stmt.setDate(1, irrigazione.getGiornoInizio());
							stmt.setDate(2, irrigazione.getGiornoFine());
							stmt.setString(3, irrigazione.getTipoIrrigazione());
							stmt.setInt(4, attivita.getID_Attivita());
							stmt.executeUpdate();
						}
				}
				return true;
		    	
		    } catch(SQLException | NumberFormatException ex) {
		        ex.printStackTrace();
		        return false;
		    } 
			
			
		}
		
		private ColturaDTO getOrCreateColtura(Connection conn, ColturaDTO coltura) throws SQLException { // crea la coltura dalla varietà
		    String insertSql = "INSERT INTO Coltura (varietà) VALUES (?) RETURNING ID_Coltura";
		    PreparedStatement stmt = conn.prepareStatement(insertSql);
		    stmt.setString(1, coltura.getVarieta());
		    ResultSet rs = stmt.executeQuery();
		    
		    if (rs.next()) {
		        coltura.setID_Coltura(rs.getInt("ID_Coltura"));
		        rs.close();
		        stmt.close();
		        return coltura;
		    }
		    
		    throw new SQLException("Impossibile creare coltura: " + coltura.getVarieta());
		}
		
		
		private void associaColturaALotto(Connection conn, LottoDTO lotto, ColturaDTO coltura, ProgettoColtivazioneDTO progetto) throws SQLException { // associa la coltura al lotto
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
		
		private ArrayList<ColtivatoreDTO> getColtivatoriLotto(LottoDTO lotto) { //Recupera il coltivatore dal lotto
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			ArrayList<ColtivatoreDTO> coltivatori = new ArrayList<>();
			
			try {
				conn = Connessione.getConnection();
				
				String sql = "SELECT c.Codice_Fiscale " +
			             "FROM Coltivatore c " +
			             "JOIN Lotto l ON c.username_proprietario = " +
			             "(SELECT username FROM Proprietario WHERE Codice_Fiscale = l.Codice_FiscalePr) " +
			             "WHERE l.ID_Lotto = ?";
				
				stmt = conn.prepareStatement(sql);
				stmt.setInt(1, lotto.getID_Lotto());
				rs = stmt.executeQuery();
			
				while (rs.next()) {
		            String codiceFiscale = rs.getString("Codice_Fiscale");
		            ColtivatoreDTO c = new ColtivatoreDTO();   
		            c.setCodiceFiscale(codiceFiscale);
		            coltivatori.add(c);
		        }
				
				return coltivatori; 
			
			} catch (SQLException ex) {
				ex.printStackTrace();
				return null;
			} 
		}	
		
		public boolean controlloProgettoChiuso(LottoDTO lotto) { //controlla se il progetto è completato
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet risultato = null;

		    try {
		    	conn = Connessione.getConnection();
		    	
		    	String sql = "SELECT done FROM Progetto_Coltivazione WHERE id_lotto = ?";
		        stmt = conn.prepareStatement(sql);
		        stmt.setInt(1, lotto.getID_Lotto());
		        risultato = stmt.executeQuery();
		        
		        boolean completato = false;
		        if(risultato.next()) {
		            completato = risultato.getBoolean("done");
		        } else { // Se non trova progetti, il lotto è libero
		            return true;
		        }
		        
		        risultato.close();
		        stmt.close();
		        
		        if(completato==false) {
		            return false;
		        }
		    	return true;
		    } catch(SQLException | NumberFormatException ex) {
		        ex.printStackTrace();
		        return false;
		    } 
			}
		
		public ArrayList<String> dateI_FProgCB(String titolo_progetto, ColtivatoreDTO coltivatore) {		
		    ArrayList<String> date = new ArrayList<>();
		    Connection conn = null;
		    PreparedStatement stmt = null;
		    ResultSet rs = null;
		    try {
		        conn = Connessione.getConnection();

		        String sql = """
		            SELECT pc.data_inizio, pc.data_fine
		            FROM progetto_coltivazione pc
		            JOIN lotto l         ON l.id_lotto = pc.id_lotto
		            JOIN proprietario p  ON p.codice_fiscale = l.codice_fiscalepr
		            JOIN coltivatore c   ON c.username_proprietario = p.username
		            WHERE c.username = ?
		              AND pc.titolo = ?
		            ORDER BY pc.data_inizio DESC
		            LIMIT 1
		        """;

		        stmt = conn.prepareStatement(sql);
		        stmt.setString(1, coltivatore.getUsername());
		        stmt.setString(2, titolo_progetto);

		        rs = stmt.executeQuery();

		        if (rs.next()) {
		            date.add(rs.getString("data_inizio"));
		            date.add(rs.getString("data_fine"));
		        }

		    } catch (SQLException ex) {
		        ex.printStackTrace();
		    } 

		    return date;
		}
		
		//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________
		
			//      _________________ VISUALIZZA PROGETTI _________________
		
		public boolean terminaProgetto(ProgettoColtivazioneDTO progetto, LottoDTO lotto) { //Libera un lotto da un progetto di coltivazione e tutti i suoi riferimenti
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet risultato = null;
			
			try {
					conn = Connessione.getConnection(); 
					@SuppressWarnings("unused")
					int rows = 0;
					
					// segno il progetto come completato con la flag done
			        String sql1 = "UPDATE Progetto_Coltivazione SET done = true WHERE id_lotto = ? AND titolo = ? ";
			        stmt = conn.prepareStatement(sql1);
			        stmt.setInt(1, lotto.getID_Lotto());
			        stmt.setString(2, progetto.getTitolo());
			        rows = stmt.executeUpdate();
			        stmt.close();
			        
			        //segno l'attività come completata
			        String sql2 = "UPDATE Attivita SET stato = 'completata' WHERE id_lotto = ? ";
			        stmt = conn.prepareStatement(sql2);
			        stmt.setInt(1, lotto.getID_Lotto());
			        rows = stmt.executeUpdate();
			        stmt.close();
					
					//ricavo l'id dell'attività in modo da collegare le 3 attività
			        String sqlAttivita = "SELECT id_attivita FROM Attivita WHERE id_lotto = ? ";
			        stmt = conn.prepareStatement(sqlAttivita);
			        stmt.setInt(1, lotto.getID_Lotto());
			        risultato = stmt.executeQuery();
			        
			        List<Integer> idAttivitaList = new ArrayList<>();
			        
			        while (risultato.next()) {
			            idAttivitaList.add(risultato.getInt("id_attivita"));
			        }
			        risultato.close();
			        stmt.close();
			        
			        
			        for (int idAttivita : idAttivitaList) { //per ogni id dell'attività, segna ogni attività come completata
			            String sql3 = "UPDATE Semina SET stato = 'completata' WHERE id_attivita = ?"; // Segna semina come completata
			            stmt = conn.prepareStatement(sql3);
			            stmt.setInt(1, idAttivita);
			            stmt.executeUpdate();
			            stmt.close();
			            
			            String sql4 = "UPDATE Irrigazione SET stato = 'completata' WHERE id_attivita = ?"; 	// Segna irrigazione come completata
			            stmt = conn.prepareStatement(sql4);
			            stmt.setInt(1, idAttivita);
			            stmt.executeUpdate();
			            stmt.close();
			            
			            String sql5 = "UPDATE Raccolta SET stato = 'completata' WHERE id_attivita = ?"; // Segna raccolta come completata
			            stmt = conn.prepareStatement(sql5);
			            stmt.setInt(1, idAttivita);
			            stmt.executeUpdate();
			            stmt.close();
			        } 
		 
					return true;
			}  catch (SQLException | NumberFormatException ex) {
				ex.printStackTrace();
				return false;
			} 
		}
		
		
		public void popolaDatiProgetto(ProgettoColtivazioneDTO progetto) { //popola la combobox del progetto, il text field di data inizio, data fine, stima raccolto
				Connection conn = null;
				PreparedStatement stmt = null;
				ResultSet risultato = null;
				
				try {
				conn = Connessione.getConnection(); 
				
				String sql = "SELECT stima_raccolto, data_inizio, data_fine FROM view_raccolto WHERE titolo = ?";  //recupera tutti i dati del progetto tramite la view
				
				stmt = conn.prepareStatement(sql);   
				stmt.setString(1, progetto.getTitolo());
				risultato = stmt.executeQuery();
				
				if (risultato.next()) {
		            double stima = risultato.getDouble("stima_raccolto");
		            java.sql.Date sqlDataInizio = risultato.getDate("data_inizio");
		            java.sql.Date sqlDataFine = risultato.getDate("data_fine");
		            progetto.setStimaRaccolto(stima);
		            progetto.setDataInizio(sqlDataInizio);
		            progetto.setDataFine(sqlDataFine);
		            
		         }
				
				} catch (SQLException | NumberFormatException ex) {
					ex.printStackTrace();
				} 
		}
		
	    public boolean isCompletata(ProprietarioDTO proprietario, ProgettoColtivazioneDTO progetto) {  //controlla se il progetto è completato
	    	Connection conn = null;
	        PreparedStatement stmt = null;
	        ResultSet risultato = null;
	    	
	    	try {
	    		conn = Connessione.getConnection();
	    		
	    		String sql = "SELECT pc.done " +
		                    "FROM Progetto_Coltivazione pc " +
		                    "JOIN Lotto l ON l.ID_Lotto = pc.ID_Lotto " +
		                    "JOIN Proprietario p ON l.Codice_FiscalePr = p.Codice_Fiscale " +
		                    "WHERE p.username = ? AND pc.titolo = ?";
	        
				 stmt = conn.prepareStatement(sql);   
				 stmt.setString(1, proprietario.getUsername());
				 stmt.setString(2, progetto.getTitolo());
				 risultato = stmt.executeQuery();

				 if (risultato.next()) {
			            boolean isDone = risultato.getBoolean("done"); 
			            progetto.setDone(isDone); 
			            return isDone; 
			        } else {
			            return false; 
			        }
			    	
	    } catch (SQLException ex) {
	    	ex.printStackTrace();
	    	return false;
	    } 
	 }   
			//      _________________ VISUALIZZA PROGETTI _________________
}
