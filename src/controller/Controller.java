package controller;
import dto.*;
import dao.*;
import utils.method;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import gui.HomePageColtivatore;
import gui.HomePageProprietario;

	
public class Controller {
//                      _________________ LOGIN _________________
	
    public boolean[] login(String username, String password) { //effettua l'autenticazione dell'utente
    	
    	
        boolean[] lista = new boolean[3]; // [0]=true (user e password)campiOK, [1]=proprietario, [2]=coltivatore

        if (username == null || username.trim().isEmpty()
            || password == null || password.trim().isEmpty())
        {
            lista[0] = false;            // campi non validi
            return lista;           
        }

        lista[0] = true;  // campi ok
        
        utente logUser = new utente(username, password);
        lista[1] = LoginD.authP(logUser); 
        lista[2] = LoginD.authC(logUser); 
        
      //riconosce il tipo di utente 
        if (lista[1]) {
            Proprietario p = (Proprietario) LoginD.creazioneUtente(logUser);
            method.setProprietarioLoggato(p);
        }

        if (lista[2]) {
            Coltivatore c = (Coltivatore) LoginD.creazioneUtente(logUser);
        }
        
        return lista;
    }
    
    
    public void LoginResult(JFrame loginFrame, boolean[] check) { //controlli generici
        if (check[0]==false) {
            JOptionPane.showMessageDialog(loginFrame, "\n USERNAME E/O PASSWORD \n RISULTANO VUOTI O NULLI");
        } else if (check[0] && check[1] && !check[2]) {
            loginFrame.setVisible(false);
			HomePageProprietario homeP = new HomePageProprietario();
			homeP.setVisible(true);
            
        } else if (check[0] && !check[1] && check[2]) {
            loginFrame.setVisible(false);
            HomePageColtivatore homeC = new HomePageColtivatore();
            homeC.setVisible(true);
            
        } else if (check[0] && !check[1] && !check[2]) {
            JOptionPane.showMessageDialog(loginFrame, " Username o Password errati!! ");
        }
    	
    }
//                      _________________ LOGIN _________________
    
//                     _________________ REGISTRAZIONE UTENTE _________________
    
    public boolean[] registra(String nome, String cognome, String username, 
			  				  String password, String cf, String ruolo, String usernameProprietario) { //effettua la registrazione dell'utente
	boolean[] result = new boolean[4];
	result[0] = false; // non è proprietario
	result[1] = false; // non è coltivatore
	result[2] = false; // registrazione non riuscita
	result[3] = false; // user non esiste (inizialmente)

	utente user = new utente(username);
    result[3] = LoginD.usernameEsiste(user); // Verifica se l'username esiste

	if (result[3] == true) {
		return result; // Esce se username esiste
	}

	if (ruolo.equals("Proprietario")) {
		result[0] = true; // è proprietario
		Proprietario proprietario = new Proprietario(username, password, nome, cognome, cf);
		result[2] = ProprietarioD.registraP(proprietario); // esito registrazione
		ProprietarioD.aggiungiL(proprietario);
	} else if (ruolo.equals("Coltivatore")) {
		result[1] = true; // è coltivatore
		Coltivatore coltivatore = new Coltivatore(username, password, nome, cognome, cf, usernameProprietario);
		result[2] = ColtivatoreD.registraC(coltivatore); // esito registrazione

  }
	return result;
}

    public ArrayList<String> popolaComboProprietari() { //popola la combobox dei proprietari
    	return ProprietarioD.popolaComboProprietari();
	}
   
//                       _________________ REGISTRAZIONE UTENTE _________________
    
    
//                        _________________ HOMEPAGE PROPRIETARIO _________________
    
   public boolean aggiungiL(Proprietario proprietario) { //aggiunge il primo lotto disponibile
		return ProprietarioD.aggiungiL(proprietario);
	}
 //                       _________________ HOMEPAGE PROPRIETARIO _________________
   
   
//                        _________________ CREAZIONE NOTIFICA _________________
   

public boolean dividiUsername(String usernameProprietario, String usernameConcatenati, 
							  Date data, String titolo, String descrizione) {	// viene chiamato se la spunta "tutti i coltivatori" è disattivata		
	
				// Split della stringa
				String[] usernamesArray = usernameConcatenati.split(",");	        
	        
				// Converti l'array in ArrayList
				ArrayList<String> usernamesList = new ArrayList<>(Arrays.asList(usernamesArray));
	       
				
				// Crea un arraylist contenenti tutti i coltivatori che appartengono al proprietario loggato
				Proprietario proprietario = new Proprietario(usernameProprietario);
				ArrayList<String> coltivatoriProprietario= NotificaD.getColtivatoriByProprietario(proprietario);
				
				// Verifica se i coltivatori appartengono al proprietario loggato
				for(int i = 0; i < usernamesList.size(); i++) {
			        if (!coltivatoriProprietario.contains(usernamesList.get(i))) {
			            return false; 
			        }
			    }
				
				for(int i = 0; i < usernamesList.size(); i++) {
					Notifica notifica = new Notifica(titolo, descrizione, data, usernamesList.get(i));
					NotificaD.Inserisci_NotificaDB(notifica);
				}
				
				return true;

	    }

public void dividiUsernameTutti(String usernameProprietario, Date data, String titolo, String descrizione) { //viene chiamato se la spunta "tutti i coltivatori" è attivata		
	
		Proprietario proprietario = new Proprietario(usernameProprietario);
		String usernameConcatenati=NotificaD.getDestinatariUsernamesByProprietario(proprietario);
		
		// Split della stringa
		String[] usernamesArray = usernameConcatenati.trim().split(",");	        
	
		// Converti l'array in ArrayList
		ArrayList<String> usernamesList = new ArrayList<>(Arrays.asList(usernamesArray));
	
		// Chiamo il dao per ogni utente
		for(int i = 0; i < usernamesList.size(); i++) {
			Notifica notifica = new Notifica(titolo, descrizione, data, usernamesList.get(i));
			NotificaD.Inserisci_NotificaDB(notifica);
		}

}

public ArrayList <String> getColtivatoriByProprietario(String usernameProprietario) { //restituisce i coltivatori appartenenti ad un dato proprietario
	Proprietario proprietario = new Proprietario(usernameProprietario);
	return NotificaD.getColtivatoriByProprietario(proprietario);
}

public boolean controllaUsername(String username) { //controlla l'esistenza di un username di un coltivatore
	utente user = new utente(username);
	return LoginD.usernameEsiste(user);
}

   
//                         _________________ CREAZIONE NOTIFICA _________________
   
   
//                          _________________ TITOLO _________________
   
   		//CODICE

//                           _________________ TITOLO _________________
   
   
//                           _________________ TITOLO _________________
		
		//CODICE
		
//                            _________________ TITOLO _________________
}
