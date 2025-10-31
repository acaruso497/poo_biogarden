package utils;



public class method {
	
	public static String usernameGlobale;
	
	
	public static String getUsernameGlobale() {
		return usernameGlobale;
	}
	//Imposta l'username della persona loggata
	public static void setUsernameGlobale(String usernameGlobale) {
		method.usernameGlobale =usernameGlobale;
	}

	//Restituisce il Codice Fiscale del proprietario loggato
//    public static String getCodiceFiscaleByUsername(String username) {
//    	return dao.getCodiceFiscaleByUsername(username);
//    }
}
