package dto;

import java.sql.Date;

import dao.*;

public class RaccoltaDTO extends AttivitaDTO{
	private Date giornoInizio;
	private Date giornoFine;
	private String stato;
	
	
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
	
	public RaccoltaDTO(String stato) {
		this.stato=stato;
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

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
	
	public boolean sommaRaccolto(String raccolto, String coltura, String progetto) {
	    return RaccoltaDAO.sommaRaccolto(raccolto, coltura, progetto);
	}
	
	
	

}
