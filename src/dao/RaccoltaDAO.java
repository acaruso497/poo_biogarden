package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

import database.Connessione;
import dto.*;

public class RaccoltaDAO {
	// da vedeere se serve progetto coltivazione dto da chiedere 
	
	public static boolean sommaRaccolto(String raccolto,  ColturaDTO coltura, ProgettoColtivazioneDTO progetto) {
	    Connection conn = null;
	    PreparedStatement ps = null;
	    try {
	        int nuovo = Integer.parseInt(raccolto);

	        conn = Connessione.getConnection();
	        conn.setAutoCommit(false);

	        String sql =
	            "UPDATE Coltura " +
	            "SET " +
	            "  raccoltoprodotto = raccoltoprodotto + ?," +
	            "  max = GREATEST(max, ?)," +
	            "  min = CASE WHEN min = 0 OR ? < min THEN ? ELSE min END," +
	            "  counter = counter + 1," +
	            "  avg = (raccoltoprodotto + ?) / (counter + 1) " +  
	            "WHERE varietà = ?";

	        ps = conn.prepareStatement(sql);
	        ps.setInt(1, nuovo);
	        ps.setInt(2, nuovo);
	        ps.setInt(3, nuovo);
	        ps.setInt(4, nuovo);
	        ps.setInt(5, nuovo);
	        ps.setString(6, coltura.getVarieta());

	        int rows = ps.executeUpdate();
	        conn.commit();
	        return rows > 0;

	    } catch (Exception e) {
	        try { if (conn != null) conn.rollback(); } catch (Exception ignore) {}
	        e.printStackTrace();
	        return false;
	    } finally {
	        try { if (ps != null) ps.close(); } catch (Exception ignore) {}
	        try { if (conn != null) conn.setAutoCommit(true); } catch (Exception ignore) {}
	        try { if (conn != null) conn.close(); } catch (Exception ignore) {}
	    }
	}

}
