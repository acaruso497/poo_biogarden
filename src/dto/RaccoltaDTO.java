package dto;

import java.sql.Date;



public class RaccoltaDTO extends AttivitaDTO{
	
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
	
	
	
	
	
	

}
