package controller;
import dto.*;
import dao.*;
import utils.SplitUtils;
import utils.method;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import gui.HomePageColtivatore;
import gui.HomePageProprietario;

public class Controller {
	private int countSemina = 0;
    private int countIrrigazione = 0;
    private int countRaccolta = 0;
	
//                      _________________ LOGIN _________________
	
    public boolean login(String username, String password) { //effettua l'autenticazione dell'utente   	
        boolean check = false; // [0]=true (user e password)campiOK, [1]=proprietario, [2]=coltivatore

        if (username == null || username.trim().isEmpty()
            || password == null || password.trim().isEmpty())
        {
            check = false;            // campi non validi
            return check;           
        }
        
        check = true;  // campi ok      
        return check;
    }
    
    
    public boolean creaUtente(boolean check, JFrame loginFrame) {
    	
    	if(check==true) {
    		ProprietarioDTO p = new ProprietarioDTO(method.getUsernameGlobale(), method.getPsw());
            boolean ruolo = p.autentica();
            if(ruolo==true) { 
            	method.setProprietarioLoggato(p); 
            	System.out.println(method.getUsernameGlobale() + p.getNome() + p.getCognome()); 
            	HomePageProprietario homeP = new HomePageProprietario();
            	homeP.setVisible(true);
            	loginFrame.setVisible(false);
            	return true;
            	
            } 
            else if(ruolo==false) {
            	ColtivatoreDTO c = new ColtivatoreDTO(method.getUsernameGlobale(), method.getPsw());
            	boolean ruolo2 = c.autentica();
            	if(ruolo2) { 
            		method.setColtivatoreLoggato(c); 
            		System.out.println(method.getUsernameGlobale() + c.getNome() + c.getCognome()); 
            		HomePageColtivatore homeC = new HomePageColtivatore();
            		homeC.setVisible(true);
            		loginFrame.setVisible(false);
            		return true;
            	}
            }
    	}
    	
    	return false; //non trova nessun utente
          
    }
  //AGGIUNTO
    public ProprietarioDTO getProprietario (ProprietarioDTO proprietario) {
    	return ProprietarioDAO.getProprietario(proprietario);
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

    result[3] = UtenteDAO.usernameEsiste(username); // Verifica se l'username esiste

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
		ColtivatoreDTO coltivatore = new ColtivatoreDTO(
									username, password, 
									nome, cognome, cf, 
									usernameProprietario);
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

//  !!!	sposta nell oggetto utente opp in coltivatore proprietario opp utils!!
//	public boolean controllaUsername(String username) { //controlla l'esistenza di un username di un coltivatore
//		UtenteDTO user = new UtenteDTO(username);
//		return UtenteDAO.usernameEsiste(user);
//	}

   
//                         _________________ CREAZIONE NOTIFICA _________________
   
   
//                          _________________ CREAZIONE PROGETTO _________________
   
	 //Crea il progetto di coltivazione inserendo i parametri tramite dao 
    public boolean creaProgetto(ProgettoColtivazioneDTO progetto, ArrayList<String> coltureString, LottoDTO lotto) {
    	
    	
    	ArrayList<ColturaDTO> coltureDTOList = new ArrayList<>();
    	
        if (coltureString != null && !coltureString.isEmpty()) {
        for (int i = 0; i < coltureString.size(); i++) {
            String coltura = coltureString.get(i);
            String colturaPulita = coltura.trim();
            
            ColturaDTO col = new ColturaDTO(colturaPulita);
            coltureDTOList.add(col);
            
        }
    }   
        boolean ok = ProgettoColtivazioneDAO.registraProgetto(progetto, lotto, coltureDTOList);        
        return ok;	    
    }
    
    public boolean creaAttivita(SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta, LottoDTO lotto, ProgettoColtivazioneDTO progetto) {
       	boolean insertAttivita = ProgettoColtivazioneDAO.insertAttivita(semina, irrigazione, raccolta, lotto, progetto);
      	return insertAttivita;
        }
    
    public List<String> getLottiByProprietario(ProprietarioDTO proprietario){ //popola la combobox dei lotti
    	ProprietarioDAO dao = new ProprietarioDAO();
    	return dao.getLottiByProprietario(proprietario);
    }
    
    private void incrementaContatore(String tipoAttivita) {   //incrementa il contatore delle attività
        if ("Semina".equals(tipoAttivita)) {
            countSemina++;
        } else if ("Irrigazione".equals(tipoAttivita)) {
            countIrrigazione++;
        } else if ("Raccolta".equals(tipoAttivita)) {
            countRaccolta++;
        }
    }
      
    public void resetContatori() {     // Reset dei contatori (utile quando si va alla prossima fase)
        countSemina = 0;
        countIrrigazione = 0;
        countRaccolta = 0;
    }
    
    public boolean controlloProgettoChiuso(LottoDTO lotto) { //controlla se il progetto è completato
    	ProgettoColtivazioneDAO dao = new ProgettoColtivazioneDAO();
    	return dao.controlloProgettoChiuso(lotto);
    	
    }
    
    
//                            _________________ CREAZIONE PROGETTO _________________

//  	_________________ HomePagecoltivatore _________________
    
    public ColtivatoreDTO getColtivatore(ColtivatoreDTO coltivatore) {
    	return ColtivatoreDAO.getColtivatore(coltivatore);
    }
	public boolean legginotifiche(ColtivatoreDTO coltivatore) {
        return ColtivatoreDAO.segnaNotificheColtivatoreComeLette(coltivatore);
    }
	public boolean checknotifiche(ColtivatoreDTO coltivatore) {
        return ColtivatoreDAO.ciSonoNotificheNonLette(coltivatore);
    }
	public String mostranotifiche(ColtivatoreDTO coltivatore) {
        return ColtivatoreDAO.getNotificheNonLette(coltivatore);
    }

    public List<String> popolaPrComboBox(ColtivatoreDTO coltivatore) {
        return ColtivatoreDAO.popolaProgettiCB(coltivatore);
    }
    // probabilmente da qui in giu si sposta tutto il dao nel dao progetto   ____INIZIO
public List<String> DateInizioFineP(String titolo_progetto, ColtivatoreDTO coltivatore) {
        return ColtivatoreDAO.dateI_FProgCB(titolo_progetto, coltivatore);
}

public List<String> getTipiAttivita(ColtivatoreDTO coltivatore, String progetto) {
    return ColtivatoreDAO.getTipiAttivitaColtivatore(coltivatore, progetto);
}

public List<String> getIdAttivita(ColtivatoreDTO coltivatore, String progetto) {
    return ColtivatoreDAO.getIdAttivitaColtivatore(coltivatore, progetto);
}

	
public String[] getDateByAttivitaId(String idAttivita, String tipoAttivita) {
    return ColtivatoreDAO.getDateByAttivitaId(idAttivita, tipoAttivita);
}

public String getLottoEPosizioneByProgetto(String progetto, ColtivatoreDTO coltivatore) {
    return ColtivatoreDAO.getLottoEPosizione(progetto, coltivatore);
}

public String getStimaRaccolto(ColtivatoreDTO coltivatore, String progetto) {
    return ColtivatoreDAO.getStimaRaccolto(coltivatore, progetto);
}

public String getIrrigazione(ColtivatoreDTO coltivatore, String progetto) {
    return ColtivatoreDAO.getIrrigazione(coltivatore, progetto);
} 

public String getTipoSemina(String idSemina) {
    return ColtivatoreDAO.getTipoSemina(idSemina);
}

public boolean sommaRaccolto(String raccolto, String coltura, String progetto) {
    return ColtivatoreDAO.sommaRaccolto(raccolto, coltura, progetto);
}

public List<String> getColtura(ColtivatoreDTO coltivatore, String progetto) {
    return ColtivatoreDAO.getColtura(coltivatore, progetto);
}
// probabilmente da qui in giu si sposta tutto il dao nel dao progetto FINE 
    
//  	_________________ HomePagecoltivatore _________________
    
//                           _________________ TITOLO _________________
   
 
    
   
//                           _________________ TITOLO _________________
		
		//CODICE
		
//                            _________________ TITOLO _________________
}
