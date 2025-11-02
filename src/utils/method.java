package utils;

import dto.Proprietario;

public class method {
	
	private static String usernameGlobale;
	private static String CF;
	private static Proprietario proprietarioLoggato;
	
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
	public static Proprietario getProprietarioLoggato() {
		return proprietarioLoggato;
	}

	//Imposta il proprietario loggato
	public static void setProprietarioLoggato(Proprietario proprietario) {
		proprietarioLoggato = proprietario;
	}
		
	
} 
