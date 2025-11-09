package dto;

import java.sql.Date;

public class RaccoltaDTO extends AttivitaDTO{
	private Date giornoInizio;
	private Date giornoFine;
	
	public RaccoltaDTO() {}

	 public RaccoltaDTO(Date giornoInizio, Date giornoFine) {
	     this.giornoInizio = giornoInizio;
	     this.giornoFine = giornoFine;
	 }
	
	public RaccoltaDTO(int ID_Attivita, String codiceFiscaleCol, Date giornoInizio, Date giornoFine) {
		super(ID_Attivita, codiceFiscaleCol);
		this.giornoInizio=giornoInizio;
		this.giornoFine=giornoFine;
	}
	

	public Date getGiornoInizio() {
		return giornoInizio;
	}

	public void setGiornoInizio(Date giornoInizio) {
		this.giornoInizio = giornoInizio;
	}

	public Date getGiornoFine() {
		return giornoFine;
	}

	public void setGiornoFine(Date giornoFine) {
		this.giornoFine = giornoFine;
	}
	
	
}
