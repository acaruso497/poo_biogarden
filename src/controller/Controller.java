package controller;
import dto.*;
import dao.*;
import utils.SplitUtils;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Controller {
private Integer countSemina;
private Integer countIrrigazione;
private Integer countRaccolta;
private ColtivatoreDAO ColtivatoreDAO = new ColtivatoreDAO();
private ColturaDAO ColturaDAO = new ColturaDAO();
private IrrigazioneDAO IrrigazioneDAO = new IrrigazioneDAO();
private NotificaDAO NotificaDAO = new NotificaDAO();
private ProgettoColtivazioneDAO ProgettoColtivazioneDAO = new ProgettoColtivazioneDAO();
private ProprietarioDAO ProprietarioDAO = new ProprietarioDAO();
private RaccoltaDAO RaccoltaDAO = new RaccoltaDAO();
private SeminaDAO SeminaDAO = new SeminaDAO();
private UtenteDAO UtenteDAO = new UtenteDAO();

	
//                      _________________ LOGIN _________________
    public ProprietarioDTO getProprietario (ProprietarioDTO proprietario) {
    	return ProprietarioDAO.getProprietario(proprietario);
    }   

	public boolean autentica(ColtivatoreDTO coltivatore) {
		return ColtivatoreDAO.authC(coltivatore);
	}
	public boolean autentica(ProprietarioDTO proprietario) {
		return ProprietarioDAO.authP(proprietario);
	}

public int login(String username, String password) { 
	// Restituisce: 
	// 0 = campi non validi (vuoti/null)
	// 1= credenziali errate (username/password sbagliati)
	// 2 = proprietario
	// 3= coltivatore
	
	// Controllo campi vuoti
	if (username == null || username.trim().isEmpty()
		|| password == null || password.trim().isEmpty()) {
		return 0; 
	}
	
	// Prima verifica se è proprietario
	ProprietarioDTO p = new ProprietarioDTO(username, password);
	boolean isProprietario = ProprietarioDAO.authP(p);
	if (isProprietario) {
		return 2; 
	}
	
	// Se non è proprietario, verifica se è coltivatore
	ColtivatoreDTO c = new ColtivatoreDTO(username, password);
	boolean isColtivatore = ColtivatoreDAO.authC(c);
	if (isColtivatore) {
		return 3; 
	}
	
	return 1; 
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
   
//                        __________________ CREAZIONE NOTIFICA _________________
   
	public boolean dividiUsername(String usernameProprietario, String usernameConcatenati,
            					  Date data, String titolo, String descrizione) { // viene chiamato se la spunta "tutti i coltivatori" è disattivata

		ArrayList<String> usernamesList = SplitUtils.splitByCommaToArrayList(usernameConcatenati); // Split della stringa + conversione in ArrayList (utils)

		ProprietarioDTO proprietario = new ProprietarioDTO(usernameProprietario);
		ArrayList<String> coltivatoriProprietario = ProprietarioDAO.getColtivatoriByProprietario(proprietario); // Ottieni i coltivatori del proprietario

		for (int i = 0; i < usernamesList.size(); i++) { // Verifica se i coltivatori appartengono al proprietario loggato 
			if (!coltivatoriProprietario.contains(usernamesList.get(i))) {
				return false;
			}
		}

		for (int i = 0; i < usernamesList.size(); i++) { // Inserimento notifiche
			NotificaDTO notifica = new NotificaDTO(titolo, descrizione, data, usernamesList.get(i));
			NotificaDAO.Inserisci_NotificaDB(notifica);
		}

	return true;
	}


	public void dividiUsernameTutti(String usernameProprietario, Date data, String titolo, String descrizione) { //viene chiamato se la spunta "tutti i coltivatori" è attivata		
	
		ProprietarioDTO proprietario = new ProprietarioDTO(usernameProprietario);
		String usernameConcatenati= ProprietarioDAO.getDestinatariUsernamesByProprietario(proprietario);
		
		ArrayList<String> usernamesList = SplitUtils.splitByCommaToArrayList(usernameConcatenati); // Split della stringa + conversione in ArrayList
	
		for(int i = 0; i < usernamesList.size(); i++) { // Chiamo il dao per ogni utente
			NotificaDTO notifica = new NotificaDTO(titolo, descrizione, data, usernamesList.get(i));
			NotificaDAO.Inserisci_NotificaDB(notifica);
		}
	}

	public ArrayList <String> getColtivatoriByProprietario(String usernameProprietario) { //restituisce i coltivatori appartenenti ad un dato proprietario
		ProprietarioDTO proprietario = new ProprietarioDTO(usernameProprietario);
		return ProprietarioDAO.getColtivatoriByProprietario(proprietario);
	}

	public boolean controllaUsername(String username) { //controlla l'esistenza di un username di un coltivatore
		ColtivatoreDTO coltivatore = new ColtivatoreDTO(username);
		return ColtivatoreDAO.usernameColtivatoreEsiste(coltivatore);
	}
	
	public boolean legginotifiche(ColtivatoreDTO coltivatore) {
        return NotificaDAO.segnaNotificheColtivatoreComeLette(coltivatore);
    }
	
	public boolean checknotifiche(ColtivatoreDTO coltivatore) {
        return NotificaDAO.ciSonoNotificheNonLette(coltivatore);
    }
	
	public String mostranotifiche(ColtivatoreDTO coltivatore) {
        return NotificaDAO.getNotificheNonLette(coltivatore);
    }

   
//                         _________________ CREAZIONE NOTIFICA _________________
   
//                          _________________ CREAZIONE PROGETTO _________________
   
	public boolean creaProgetto(ProgettoColtivazioneDTO progetto, ArrayList<String> coltureString, LottoDTO lotto) { //crea il progetto di coltivazione
    	
    	ProgettoColtivazioneDAO dao = new ProgettoColtivazioneDAO();
    	ArrayList<ColturaDTO> coltureDTOList = new ArrayList<>();
    	
        if (coltureString != null && !coltureString.isEmpty()) {
        for (int i = 0; i < coltureString.size(); i++) {
            String coltura = coltureString.get(i);
            String colturaPulita = coltura.trim();
            
            ColturaDTO col = new ColturaDTO(colturaPulita);
            coltureDTOList.add(col);
            
        }
    }   
        boolean ok = dao.registraProgetto(progetto, lotto, coltureDTOList);        
        return ok;	    
    }
	
	
    public boolean creaAttivita(SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta, LottoDTO lotto, ProgettoColtivazioneDTO progetto) { //crea l'attività
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
    	return ProgettoColtivazioneDAO.controlloProgettoChiuso(lotto);
    }
    
//      _________________ CREAZIONE PROGETTO _________________

//  	_________________ HomePagecoltivatore _________________
    
    public ColtivatoreDTO getColtivatore(ColtivatoreDTO coltivatore) {
    	return ColtivatoreDAO.getColtivatore(coltivatore);
    }

    public List<String> popolaPrComboBox(ColtivatoreDTO coltivatore) {
        return ColtivatoreDAO.popolaProgettiCB(coltivatore);
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
	
	public List<String> getColtura(ColtivatoreDTO coltivatore, String progetto) {
	    return ColtivatoreDAO.getColtura(coltivatore, progetto);
	}
	
	public boolean sommaRaccolto(int raccolto, ColturaDTO coltura, ProgettoColtivazioneDTO progetto) {
	    return RaccoltaDAO.sommaRaccolto(raccolto, coltura, progetto);
	}
	
	public List<String> DateInizioFineP(String titolo_progetto, ColtivatoreDTO coltivatore) {
		ProgettoColtivazioneDAO dao = new ProgettoColtivazioneDAO();
	    return dao.dateI_FProgCB(titolo_progetto, coltivatore);
	}


//  	_________________ HomePagecoltivatore _________________
    
//                           _________________ VISUALIZZA PROGETTI _________________
   
    public String getRaccoltoProdotto(ProprietarioDTO proprietario, int idLotto, ArrayList<String> coltureList){ //restituisce il raccolto prodotto della coltura
    	ArrayList<ColturaDTO> coltureDTOList = new ArrayList<>();
    	LottoDTO lotto = new LottoDTO(idLotto); 
    	
        if (coltureList != null && !coltureList.isEmpty()) {
        for (int i = 0; i < coltureList.size(); i++) {
            String coltura = coltureList.get(i);
            String colturaPulita = coltura.trim();
            
            ColturaDTO col = new ColturaDTO(colturaPulita);
            coltureDTOList.add(col);
            
        }
    	
        }
        ColturaDAO dao = new ColturaDAO();
        return dao.getRaccoltoProdotto(proprietario, lotto, coltureDTOList);
    }
    
    public boolean aggiornaStato(String stato, String tipoAttivita, LottoDTO lotto) { // Aggiorna lo stato delle attività
    	AttivitaDAO dao = new AttivitaDAO();
    	SeminaDTO semina = null;
    	IrrigazioneDTO irrigazione = null;
    	RaccoltaDTO raccolta = null;
    	
    	if(tipoAttivita.equals("Semina")) {
    		semina = new SeminaDTO(stato);
    	}else if(tipoAttivita.equals("Irrigazione")) {
    		irrigazione = new IrrigazioneDTO(stato);
    	}else if(tipoAttivita.equals("Raccolta")) {
    		raccolta = new RaccoltaDTO(stato);
    	}
    	
    	return dao.aggiornaStato(semina, irrigazione, raccolta, lotto);
    }
   
  
    public ProgettoColtivazioneDTO getProgettoByTitolo(String titolo) {
       ProgettoColtivazioneDTO progetto = new ProgettoColtivazioneDTO(titolo);
       ProgettoColtivazioneDAO.popolaDatiProgetto(progetto);
       return progetto;
    }

    public void popolaDatiProgetto(ProgettoColtivazioneDTO progetto) {  // Setta il titolo del progetto, i campi di data inizio e data fine
    	ProgettoColtivazioneDAO.popolaDatiProgetto(progetto);
    } 
    
    public boolean isCompletata(ProprietarioDTO proprietario, ProgettoColtivazioneDTO progetto) {     //controlla se il progetto è completato
    	ProgettoColtivazioneDAO dao = new ProgettoColtivazioneDAO();
    	return dao.isCompletata(proprietario, progetto);
    }
    
    public ArrayList<String> getColtureProprietario(ProprietarioDTO proprietario, ProgettoColtivazioneDTO progetto) { //restituisce le colture presenti nel lotto del progetto di coltivazione in riferimento al proprietario
    	ColturaDAO dao = new ColturaDAO();
    	return dao.getColtureProprietario(proprietario, progetto);
	}
    
    public List<String> getProgettiByProprietario(ProprietarioDTO proprietario) { // Popola ComboProgetto con il titolo del progetto del proprietario
    	ProprietarioDAO dao = new ProprietarioDAO();
        return dao.getProgettiByProprietario(proprietario);  
    }
    
    public String getLottoProgettoByProprietario(ProgettoColtivazioneDTO progetto, ProprietarioDTO proprietario) {
    	LottoDAO dao = new LottoDAO();
    	return dao.getLottoProgettoByProprietario(progetto, proprietario);
    }
    
	public boolean terminaProgetto(ProgettoColtivazioneDTO progetto, LottoDTO lotto) { //termina il progetto di coltivazione ;
		return ProgettoColtivazioneDAO.terminaProgetto(progetto, lotto);

	}
	
	public SeminaDTO getSeminaByTitolo(String titolo, Date dataIA, Date dataFA) {
		ProgettoColtivazioneDTO progetto = new ProgettoColtivazioneDTO(titolo);
		SeminaDTO semina = new SeminaDTO(dataIA, dataFA);
		SeminaDAO.popolaSemina(progetto, semina);
		
		return semina;
		
	}
	
	public IrrigazioneDTO getIrrigazioneByTitolo(String titolo, Date dataIA, Date dataFA) {
		ProgettoColtivazioneDTO progetto = new ProgettoColtivazioneDTO(titolo);
		IrrigazioneDTO irrigazione = new IrrigazioneDTO(dataIA, dataFA);
		IrrigazioneDAO.popolaIrrigazione(progetto, irrigazione);
		
		return irrigazione;
		
	}
	
	public RaccoltaDTO getRaccoltaByTitolo(String titolo, Date dataIA, Date dataFA) {
		ProgettoColtivazioneDTO progetto = new ProgettoColtivazioneDTO(titolo);
		RaccoltaDTO raccolta = new RaccoltaDTO(dataIA, dataFA);
		RaccoltaDAO.popolaRaccolta(progetto, raccolta);
		
		return raccolta;
		
	}
	
	public void popolaSemina(ProgettoColtivazioneDTO progetto, SeminaDTO semina) {
		SeminaDAO.popolaSemina(progetto, semina);
	}
	
	public void popolaIrrigazione(ProgettoColtivazioneDTO progetto, IrrigazioneDTO irrigazione) {
		IrrigazioneDAO.popolaIrrigazione(progetto, irrigazione);
	}
	
	public void popolaRaccolta(ProgettoColtivazioneDTO progetto, RaccoltaDTO raccolta) {
		RaccoltaDAO.popolaRaccolta(progetto, raccolta);
	}
	
	
//	_________________ VisualizzaProgetto _________________
	
	
//	_________________ Grafici _________________
	
	public List<String> getColturaByLotto(int idLottoStr) { // Popola ComboColtura con l'ID del lotto
		LottoDTO lotto = new LottoDTO(idLottoStr);
        return ColturaDAO.getColturaByLotto(lotto);
    }
	
    public double[] getStatistiche(String varieta) { // chiama il dao per ottenere le statistiche
    	ColturaDTO coltura = new ColturaDTO(varieta);
        long   num   = ColturaDAO.getNumeroRaccolte(coltura);
        double media = ColturaDAO.getMediaRaccolto(coltura);
        double min   = ColturaDAO.getMinRaccolto(coltura);
        double max   = ColturaDAO.getMaxRaccolto(coltura);
        return new double[]{ num, media, min, max };
    }
    
//	_________________ Grafici _________________   
		
}
