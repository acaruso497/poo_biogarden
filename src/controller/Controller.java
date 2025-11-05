package controller;
import dto.*;
import dao.*;
import utils.SplitUtils;
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
        
        UtenteDTO logUser = new UtenteDTO(username, password);
        lista[1] = ProprietarioDAO.authP(logUser); 
        lista[2] = ColtivatoreDAO.authC(logUser); 
        
      //riconosce il tipo di utente 
        if (lista[1]) {
            ProprietarioDTO p = (ProprietarioDTO) UtenteDAO.creazioneUtente(logUser);
            method.setProprietarioLoggato(p);
        }

        if (lista[2]) {
            ColtivatoreDTO c = (ColtivatoreDTO) UtenteDAO.creazioneUtente(logUser);
            method.setColtivatoreLoggato(c);
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

	UtenteDTO user = new UtenteDTO(username);
    result[3] = UtenteDAO.usernameEsiste(user); // Verifica se l'username esiste

	if (result[3] == true) {
		return result; // Esce se username esiste
	}

	if (ruolo.equals("Proprietario")) {
		result[0] = true; // è proprietario
		ProprietarioDTO proprietario = new ProprietarioDTO(username, password, nome, cognome, cf);
		result[2] = ProprietarioDAO.registraP(proprietario); // esito registrazione
		ProprietarioDAO.aggiungiL(proprietario);
	} else if (ruolo.equals("Coltivatore")) {
		result[1] = true; // è coltivatore
		ColtivatoreDTO coltivatore = new ColtivatoreDTO(username, password, nome, cognome, cf, usernameProprietario);
		result[2] = ColtivatoreDAO.registraC(coltivatore); // esito registrazione

  }
	return result;
}

    public ArrayList<String> popolaComboProprietari() { //popola la combobox dei proprietari
    	return ProprietarioDAO.popolaComboProprietari();
	}
   
//                       _________________ REGISTRAZIONE UTENTE _________________
    
    
//                        _________________ HOMEPAGE PROPRIETARIO _________________
    
   public boolean aggiungiL(ProprietarioDTO proprietario) { //aggiunge il primo lotto disponibile
		return ProprietarioDAO.aggiungiL(proprietario);
	}
//                       _________________ HOMEPAGE COLTIVATORE _________________
   
	public void legginotifiche(String usernamecoltivatore) {
		NotificaDAO DAO = new NotificaDAO();
		ColtivatoreDTO coltivatore = new ColtivatoreDTO(usernamecoltivatore);
		DAO.segnaNotificheColtivatoreComeLette(coltivatore);
    }
	
	public boolean checknotifiche(String usernamecoltivatore) {
		NotificaDAO DAO = new NotificaDAO();
		ColtivatoreDTO coltivatore = new ColtivatoreDTO(usernamecoltivatore);
        return DAO.ciSonoNotificheNonLette(coltivatore);
    }
	
	public String mostranotifiche(String usernamecoltivatore) {
		NotificaDAO DAO = new NotificaDAO();
		ColtivatoreDTO coltivatore = new ColtivatoreDTO(usernamecoltivatore);
        return DAO.getNotificheNonLette(coltivatore);
    }
   
//                        __________________ CREAZIONE NOTIFICA _________________
   

	public boolean dividiUsername(String usernameProprietario, String usernameConcatenati,
            Date data, String titolo, String descrizione) { // viene chiamato se la spunta "tutti i coltivatori" è disattivata

		// Split della stringa + conversione in ArrayList (utils)
		ArrayList<String> usernamesList = SplitUtils.splitByCommaToArrayList(usernameConcatenati);

		// Ottieni i coltivatori del proprietario (versione refactor: DTO/DAO)
		ProprietarioDTO proprietario = new ProprietarioDTO(usernameProprietario);
		ArrayList<String> coltivatoriProprietario = ProprietarioDAO.getColtivatoriByProprietario(proprietario);

		// Verifica se i coltivatori appartengono al proprietario loggato (tuo controllo)
		for (int i = 0; i < usernamesList.size(); i++) {
			if (!coltivatoriProprietario.contains(usernamesList.get(i))) {
				return false;
			}
		}

		// Inserimento notifiche (versione refactor: DTO/DAO)
		for (int i = 0; i < usernamesList.size(); i++) {
			NotificaDTO notifica = new NotificaDTO(titolo, descrizione, data, usernamesList.get(i));
			NotificaDAO.Inserisci_NotificaDB(notifica);
		}

	return true;
	}


	public void dividiUsernameTutti(String usernameProprietario, Date data, String titolo, String descrizione) { //viene chiamato se la spunta "tutti i coltivatori" è attivata		
	
		ProprietarioDTO proprietario = new ProprietarioDTO(usernameProprietario);
		String usernameConcatenati= ProprietarioDAO.getDestinatariUsernamesByProprietario(proprietario);
		
		// Split della stringa + conversione in ArrayList
		ArrayList<String> usernamesList = SplitUtils.splitByCommaToArrayList(usernameConcatenati);
	
		// Chiamo il dao per ogni utente
		for(int i = 0; i < usernamesList.size(); i++) {
			NotificaDTO notifica = new NotificaDTO(titolo, descrizione, data, usernamesList.get(i));
			NotificaDAO.Inserisci_NotificaDB(notifica);
		}
	}

	public ArrayList <String> getColtivatoriByProprietario(String usernameProprietario) { //restituisce i coltivatori appartenenti ad un dato proprietario
		ProprietarioDTO proprietario = new ProprietarioDTO(usernameProprietario);
		return ProprietarioDAO.getColtivatoriByProprietario(proprietario);
	}

	public boolean controllaUsername(String username) { //controlla l'esistenza di un username di un coltivatore
		UtenteDTO user = new UtenteDTO(username);
		return UtenteDAO.usernameEsiste(user);
	}

   
//                         _________________ CREAZIONE NOTIFICA _________________
   
   
//                          _________________ CREAZIONE PROGETTO _________________
   
	 //Crea il progetto di coltivazione inserendo i parametri tramite dao 
    public boolean creaProgetto(String titolo, String idLottoStr, String descrizione, String stimaRaccoltoStr, 
    							ArrayList<String> coltureString, Date dataIP, Date dataFP) {
    	
    	//AtomicInteger idOut = new AtomicInteger();
    	
    	ArrayList<ColturaDTO> coltureDTOList = new ArrayList<>();
    	
        if (coltureString != null && !coltureString.isEmpty()) {
        for (int i = 0; i < coltureString.size(); i++) {
            String coltura = coltureString.get(i);
            String colturaPulita = coltura.trim();
            
            ColturaDTO col = new ColturaDTO(colturaPulita);
            coltureDTOList.add(col);
            
        }
    }
    
        
//	    boolean ok = daoCreaP.registraProgetto(titolo, idLottoStr, stimaRaccoltoStr, 
//	    								 coltureString, descrizione, dataIP, dataFP, idOut);
//	    
//	    if(ok)	lastIdProgetto = idOut.get();
//	    else	lastIdProgetto = null;
//	    
//	    return ok;
	    
    }
    
//                            _________________ CREAZIONE PROGETTO _________________

    
//                           _________________ TITOLO _________________
   
   
//                           _________________ TITOLO _________________
		
		//CODICE
		
//                            _________________ TITOLO _________________
}
