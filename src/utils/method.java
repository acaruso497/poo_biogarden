package utils;

import dto.ColtivatoreDTO;
import dto.ProprietarioDTO;

public class method {
	
	private static String usernameGlobale;
	private static String CF;
	private static ProprietarioDTO proprietarioLoggato;
	private static ColtivatoreDTO coltivatoreLoggato;
	private static String psw;
	
	//Restituisce l'username del proprietario loggato
	public static String getCF() {
		return CF;
	}

	//Imposta il codice fiscale del proprietario loggato
	public static void setCF(String cF) {
		CF = cF;
	}

	//Restituisce l'username della persona loggata
	public static String getUsernameGlobale() {
		return usernameGlobale;
	}
	
	//Imposta l'username della persona loggata
	public static void setUsernameGlobale(String usernameGlobale) {
		method.usernameGlobale =usernameGlobale;
	}
	
	//Restituisce il proprietario loggato
	public static ProprietarioDTO getProprietarioLoggato() {
		return proprietarioLoggato;
	}

	//Imposta il proprietario loggato
	public static void setProprietarioLoggato(ProprietarioDTO proprietario) {
		proprietarioLoggato = proprietario;
	}

	//Restituisce il coltivatore loggato
	public static ColtivatoreDTO getColtivatoreLoggato() {
		return coltivatoreLoggato;
	}

	//Imposta il coltivatore loggato
	public static void setColtivatoreLoggato(ColtivatoreDTO coltivatoreLoggato) {
		method.coltivatoreLoggato = coltivatoreLoggato;
	}

	public static String getPsw() {
		return psw;
	}

	public static void setPsw(String psw) {
		method.psw = psw;
	}
	
	
		
	
} 
