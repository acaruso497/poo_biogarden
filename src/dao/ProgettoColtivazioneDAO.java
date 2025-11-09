package dao;

import database.Connessione;
import dto.*;
import java.sql.*;
import java.util.ArrayList;

public class ProgettoColtivazioneDAO {

	//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________	
	
	
		public static boolean registraProgetto(ProgettoColtivazioneDTO progetto, LottoDTO lotto, ArrayList<ColturaDTO> coltureDTOList) { //Registrazione dei dati del progetto
		    
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
		        
		        //pulisce le colture dalle virgole
		        if (coltureDTOList != null && coltureDTOList.size() > 0) {
		            for (int i = 0; i < coltureDTOList.size(); i++) {
		                    // Crea o recupera coltura
		                    ColturaDTO coltura = getOrCreateColtura(conn, coltureDTOList.get(i));
		                    
		                    // Associa coltura al lotto
		                    associaColturaALotto(conn, lotto, coltura, progetto);
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
		
		public static boolean insertAttivita(SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta, LottoDTO lotto, ProgettoColtivazioneDTO progetto) {
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
			
				// per ogni coltivatore, viene assegnata un'attività
				for (ColtivatoreDTO coltivatore : coltivatori) {
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
						
						if(raccolta!=null) {
							// inserisce la specifica attività ai coltivatori
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
		    } finally {
		        try { if (risultato != null) risultato.close(); } catch (Exception e) {}
		        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
		        try { if (conn != null) conn.close(); } catch (Exception e) {}
		    }
			
			
		}
		
		private static ColturaDTO getOrCreateColtura(Connection conn, ColturaDTO coltura) throws SQLException { // crea la coltura dalla varietà
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
		    
		    
		    String insertSql = "INSERT INTO Coltura (varietà) VALUES (?) RETURNING ID_Coltura";
		    stmt = conn.prepareStatement(insertSql);
		    stmt.setString(1, coltura.getVarieta());
		    rs = stmt.executeQuery();
		    
		    if (rs.next()) {
		        coltura.setID_Coltura(rs.getInt("ID_Coltura"));
		        rs.close();
		        stmt.close();
		        return coltura;
		    }
		    
		    throw new SQLException("Impossibile creare coltura: " + coltura.getVarieta());
		}
		
		
		private static void associaColturaALotto(Connection conn, LottoDTO lotto, ColturaDTO coltura, ProgettoColtivazioneDTO progetto) throws SQLException { // associa la coltura al lotto
			
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
		
		private static ArrayList<ColtivatoreDTO> getColtivatoriLotto(LottoDTO lotto) { //Recupera il coltivatore dal lotto
			Connection conn = null;
			PreparedStatement stmt = null;
			ResultSet rs = null;
			ArrayList<ColtivatoreDTO> coltivatori = new ArrayList<>();
			
			try {
				conn = Connessione.getConnection();
				//String sql = "SELECT DISTINCT a.Codice_FiscaleCol FROM Attivita a WHERE a.ID_Lotto = ? ";
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
			} finally {
				try { if (rs != null) rs.close(); } catch (Exception e) {}
				try { if (stmt != null) stmt.close(); } catch (Exception e) {}
				try { if (conn != null) conn.close(); } catch (Exception e) {}
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
		    } finally {
		        try { if (risultato != null) risultato.close(); } catch (Exception e) {}
		        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
		        try { if (conn != null) conn.close(); } catch (Exception e) {}
		    }
		    
			}
		
		
		
		//____________________   CREAZIONE PROGETTO COLTIVAZIONE     ____________________________________
		
		
	
}
