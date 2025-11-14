package dto;

import java.sql.Date;

import dao.*;

public class RaccoltaDTO extends AttivitaDTO{
	private String Raccolto;

	public String getRaccolto() {
		return Raccolto;
	}

	public void setRaccolto(String raccolto) {
		Raccolto = raccolto;
	}

	public RaccoltaDTO() {
		super();
	}

	 public RaccoltaDTO(Date giornoInizio, Date giornoFine) {
		 super();
	     this.giornoInizio = giornoInizio;
	     this.giornoFine = giornoFine;
	 }
	
	 public RaccoltaDTO(int ID_Attivita, String codiceFiscaleCol, Date giornoInizio, Date giornoFine) {
	        super(ID_Attivita, codiceFiscaleCol, giornoInizio, giornoFine);
	    }
	
	 public RaccoltaDTO(String stato) {
	        super();
	        this.stato = stato;  
	    }
	
	
	public boolean sommaRaccolto(String raccolto, ColturaDTO coltura, ProgettoColtivazioneDTO progetto) {
		this.Raccolto = raccolto;
	    return RaccoltaDAO.sommaRaccolto(Raccolto, coltura, progetto);
	}
	
	
	

}
