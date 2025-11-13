package dao;

import java.util.List;
import java.util.ArrayList;
import database.Connessione;
import dto.*;
import java.sql.*;

public class ColtivatoreDAO {
	public static ArrayList<String> idList = new ArrayList<>();
	//____________________   LOGIN     ____________________________________
	
	public static boolean authC(UtenteDTO User) { //Autenticazione coltivatore
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet risultato = null;
		try {
				conn = Connessione.getConnection(); 
				String sql = "SELECT * FROM coltivatore WHERE username=? AND psw=?";
				stmt = conn.prepareStatement(sql);
				stmt.setString(1,User.getUsername());
				stmt.setString(2, User.getPassword());
				risultato = stmt.executeQuery();
				if(risultato.next()) {
					System.out.println(" ACCESSO CONSENTITO: COLTIVATORE");
					return true;
				}else {
					System.out.println(" ACCESSO NON CONSENTITO: COLTIVATORE");
					return false;
				}
		   } catch(SQLException ex) {
			   ex.printStackTrace();
			   return false;
		   } finally {
			   try { if (risultato != null) risultato.close(); } catch (Exception e) {}
		        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
		        try { if (conn != null) conn.close(); } catch (Exception e) {}
		   }
		
	}
	
	//____________________   LOGIN     ____________________________________
	

	//____________________   REGISTRAZIONE     ____________________________________
		
	public static boolean registraC(ColtivatoreDTO coltivatore) {		// Registrazione COLTIVATORE
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
	
   //  _________________ HomePagecoltivatore _________________
	
	public static ColtivatoreDTO getColtivatore(ColtivatoreDTO credenziali) {
	    Connection conn = null;
	    PreparedStatement stmt = null;
	    ResultSet risultato = null;
	    try {
	        conn = Connessione.getConnection();
	        // Cerca il coltivatore con username e password forniti
	        String sql = "SELECT * FROM coltivatore WHERE username = ? AND psw = ?";
	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, credenziali.getUsername());
	        stmt.setString(2, credenziali.getPassword());
	        risultato = stmt.executeQuery();

	        if (risultato.next()) {
	            // Quando trovato, crea e popola un nuovo ColtivatoreDTO con tutti gli attributi
	        		credenziali = new ColtivatoreDTO(
	                risultato.getString("username"),
	                risultato.getString("psw"),
	                risultato.getString("nome"),
	                risultato.getString("cognome"),
	                risultato.getString("codice_fiscale"),
	                risultato.getString("username_proprietario"),
	                risultato.getString("esperienza")
	          
	            );
	            System.out.println("COLTIVATORE RECUPERATO CON SUCCESSO");
	            return credenziali;
	        } else {
	            System.out.println("RECUPERO COLTIVATORE FALLITO \n funzione di ColtivatoreDAO\n chiamata: getColtivatore");
	            return null; // Non trovato
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	        return null;
	    } finally {
	        try { if (risultato != null) risultato.close(); } catch (Exception e) {}
	        try { if (stmt != null) stmt.close(); } catch (Exception e) {}
	        try { if (conn != null) conn.close(); } catch (Exception e) {}
	    }
	}

public static List<String> popolaProgettiCB(ColtivatoreDTO coltivatore) {
	   List<String> lista = new ArrayList<String>();
	    String sql = """
	        SELECT t.titolo
	        FROM (
	          SELECT pc.titolo, MAX(pc.data_inizio) AS di
	          FROM coltivatore c
	          JOIN proprietario p            ON p.username = c.username_proprietario
	          JOIN lotto l                   ON l.codice_fiscalepr = p.codice_fiscale
	          JOIN progetto_coltivazione pc  ON pc.id_lotto = l.id_lotto
	          WHERE c.username = ?
	            AND pc.done = false
	            AND EXISTS (
	              SELECT 1
	              FROM attivita a
	              WHERE a.id_progetto = pc.id_progetto
	                AND a.codice_fiscalecol = c.codice_fiscale
	                AND a.stato IN ('pianificata','in corso')
	            )
	          GROUP BY pc.titolo
	        ) t
	        ORDER BY t.di DESC
	    """;

	    try (Connection conn = Connessione.getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setString(1, coltivatore.getUsername());
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) lista.add(rs.getString("titolo"));
	        }
	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	    return lista;
	}



public static ArrayList<String> getAttivitaByPr(String titolo_progetto, String usernameColtivatore) {
    ArrayList<String> tipi = new ArrayList<>();
    idList.clear();
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        conn = Connessione.getConnection();

        String sql = """
            SELECT 
                a.id_attivita,
                CASE 
                    WHEN s.id_semina IS NOT NULL THEN 'Semina'
                    WHEN i.id_irrigazione IS NOT NULL THEN 'Irrigazione'
                    WHEN r.id_raccolta IS NOT NULL THEN 'Raccolta'
                END AS tipo_attivita
            FROM coltivatore c
            JOIN proprietario p         ON p.username = c.username_proprietario
            JOIN lotto l                ON l.codice_fiscalepr = p.codice_fiscale
            JOIN progetto_coltivazione pc ON pc.id_lotto = l.id_lotto
            JOIN attivita a             ON a.id_progetto = pc.id_progetto
            LEFT JOIN semina s          ON s.id_attivita = a.id_attivita
            LEFT JOIN irrigazione i     ON i.id_attivita = a.id_attivita
            LEFT JOIN raccolta r        ON r.id_attivita = a.id_attivita
            WHERE c.username = ?
              AND pc.titolo = ?
              AND pc.done = false
            ORDER BY a.giorno_inizio
        """;

        stmt = conn.prepareStatement(sql);
        stmt.setString(1, usernameColtivatore);
        stmt.setString(2, titolo_progetto);
        rs = stmt.executeQuery();

        while (rs.next()) {
            String tipo = rs.getString("tipo_attivita");
            String id = rs.getString("id_attivita");

            if (tipo != null && id != null) {
                tipi.add(tipo);
                idList.add(id);
            }
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    } finally {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
        try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
    }

    return tipi;
}


public static List<String> getTipiAttivitaColtivatore(ColtivatoreDTO coltivatore, String progetto) {
    List<String> tipoList = new ArrayList<>();

    String sql = "SELECT tipo_attivita " +
                 "FROM DateAttivitaColtivatore " +
                 "WHERE username = ? AND titolo = ? AND done = false " +
                 "ORDER BY " +
                 "CASE tipo_attivita " +
                 "    WHEN 'Semina' THEN 1 " +
                 "    WHEN 'Irrigazione' THEN 2 " +
                 "    WHEN 'Raccolta' THEN 3 " +
                 "END";

    try (Connection conn = Connessione.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, coltivatore.getUsername());
        stmt.setString(2, progetto);
        
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                tipoList.add(rs.getString("tipo_attivita"));
            }
        }
        
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
    return tipoList;
}
public static List<String> getIdAttivitaColtivatore(ColtivatoreDTO coltivatore, String progetto) {
    List<String> idList = new ArrayList<>();
    
        
    try (Connection conn = Connessione.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT tipo_attivita, id_attivita " +
                "FROM DateAttivitaColtivatore " +
                "WHERE username = ? AND titolo = ? AND done = false AND stato IN ('pianificata', 'in corso')" +
                "ORDER BY giorno_inizio")) {
    
        stmt.setString(1, coltivatore.getUsername());
        stmt.setString(2, progetto);
        ResultSet rs = stmt.executeQuery();

        
        while (rs.next()) {
            String tipoAttivita = rs.getString("tipo_attivita");
            int idAttivita = rs.getInt("id_attivita");
            idList.add(tipoAttivita + "-" + idAttivita);
        }
        
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return idList;
}
public static String[] getDateByAttivitaId(String idAttivita, String tipoAttivita) {
    String[] date = new String[2];
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        conn = Connessione.getConnection();
        // Query DINAMICA in base al tipo di attività
        
         String sql = "";
        switch(tipoAttivita) {
            case "Semina":
  
            	sql = "SELECT giorno_inizio, giorno_fine FROM DateAttivitaColtivatore WHERE id_attivita = ? AND done=false AND tipo_attivita = 'Semina'";
                break;
                
            case "Irrigazione":
                
            	sql = "SELECT giorno_inizio, giorno_fine FROM DateAttivitaColtivatore WHERE id_attivita = ? AND done=false AND tipo_attivita = 'Irrigazione'";
                break;
                
            case "Raccolta":
                
            	sql = "SELECT giorno_inizio, giorno_fine FROM DateAttivitaColtivatore WHERE id_attivita = ? AND done=false AND tipo_attivita = 'Raccolta'";
                break;
                
            default:
                return date;
        }       
        stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(idAttivita));
        rs = stmt.executeQuery();

        if (rs.next()) {
        	date[0] = rs.getString(1); // Prima colonna (data inizio)
            date[1] = rs.getString(2); // Seconda colonna (data fine)
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    } catch (NumberFormatException e) {
        System.err.println("ID attivita non valido: " + idAttivita);
    } finally {
        try { if (rs != null) rs.close(); } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close(); } catch (SQLException ignored) {}
        try { if (conn != null) conn.close(); } catch (SQLException ignored) {}
    }
    
    return date;
}

public static String getLottoEPosizione(String progetto, ColtivatoreDTO coltivatore) {
    String risultato = "";
    
    try (Connection conn = Connessione.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT id_lotto, posizione " +
                "FROM VisualizzaLottoColtivatore " +
                "WHERE titolo_progetto = ? AND username_coltivatore = ?")) {
        
        stmt.setString(1, progetto);
        stmt.setString(2, coltivatore.getUsername());
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            risultato = "Lotto: " + rs.getInt("id_lotto") + ", Posizione: " + rs.getInt("posizione");
        }
        
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    
    return risultato;
}
public static String getStimaRaccolto(ColtivatoreDTO coltivatore, String progetto) {
    String stima = "";
        
    try (Connection conn = Connessione.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT stima_raccolto FROM stima_raccoltoColtivatore " +
                "WHERE username_coltivatore = ? AND titolo_progetto = ?")) {
    
        stmt.setString(1, coltivatore.getUsername());
        stmt.setString(2, progetto);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            stima = rs.getString("stima_raccolto");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return stima;
}
public static String getIrrigazione(ColtivatoreDTO coltivatore, String progetto) {
    String irrigazione = "";
    
    try (Connection conn = Connessione.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
            		"SELECT DISTINCT tipo_irrigazione FROM irrigazione_coltivatore " +
                    "WHERE username_coltivatore = ? AND titolo_progetto = ?")) {
        
        stmt.setString(1, coltivatore.getUsername());
        stmt.setString(2, progetto);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            irrigazione = rs.getString("tipo_irrigazione");
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    return irrigazione;
}
public static String getTipoSemina(String idSemina) {
    String tipoSemina = "";
    
    try (Connection conn = Connessione.getConnection();
            PreparedStatement stmt = conn.prepareStatement(
                "SELECT tipo_semina FROM Semina WHERE id_attivita = ?")) {
        
        stmt.setInt(1, Integer.parseInt(idSemina));
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            tipoSemina = rs.getString("tipo_semina");
        }
        
    } catch (SQLException | NumberFormatException ex) {
        ex.printStackTrace();
    }
    
    return tipoSemina != null ? tipoSemina : "";
}
//public static boolean sommaRaccolto(String raccolto, String coltura, String progetto) {
//    Connection conn = null;
//    PreparedStatement ps = null;
//    try {
//        int nuovo = Integer.parseInt(raccolto);
//
//        conn = Connessione.getConnection();
//        conn.setAutoCommit(false);
//
//        String sql =
//            "UPDATE Coltura " +
//            "SET " +
//            "  raccoltoprodotto = raccoltoprodotto + ?," +
//            "  max = GREATEST(max, ?)," +
//            "  min = CASE WHEN min = 0 OR ? < min THEN ? ELSE min END," +
//            "  counter = counter + 1," +
//            "  avg = (raccoltoprodotto + ?) / (counter + 1) " +  // usa i valori PRIMA dell'update a destra
//            "WHERE varietà = ?";
//
//        ps = conn.prepareStatement(sql);
//        ps.setInt(1, nuovo);
//        ps.setInt(2, nuovo);
//        ps.setInt(3, nuovo);
//        ps.setInt(4, nuovo);
//        ps.setInt(5, nuovo);
//        ps.setString(6, coltura);
//
//        int rows = ps.executeUpdate();
//        conn.commit();
//        return rows > 0;
//
//    } catch (Exception e) {
//        try { if (conn != null) conn.rollback(); } catch (Exception ignore) {}
//        e.printStackTrace();
//        return false;
//    } finally {
//        try { if (ps != null) ps.close(); } catch (Exception ignore) {}
//        try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ignore) {}
//        try { if (conn != null) conn.close(); } catch (Exception ignore) {}
//    }
//}
public static List<String> getColtura(ColtivatoreDTO coltivatore, String progetto) {
	List<String> lista = new ArrayList<>();
	
	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet risultato = null;
	
	try {
		  conn = Connessione.getConnection();

		  String sql = "SELECT varieta_coltura " +
                  "FROM ComboTipologiaColturaColtivatore " +
                  "WHERE username_coltivatore = ? AND titolo_progetto = ?";
		 
	 
		 stmt = conn.prepareStatement(sql); 
		 stmt.setString(1, coltivatore.getUsername());
		 stmt.setString(2, progetto);
		 risultato= stmt.executeQuery();
	  
	  while (risultato.next()) {
		  String varieta = risultato.getString("varieta_coltura");
		  lista.add(String.valueOf(varieta));
	  }
	  
	} catch (SQLException ex) {
	  ex.printStackTrace();
	} finally {
	    try { if (risultato != null) risultato.close(); } catch (Exception ignored) {}
	    try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
	    try { if (conn != null) conn.close(); } catch (Exception ignored) {}
	}


	return lista;
}
}

  //  _________________ HomePagecoltivatore _________________

