package dto;

import java.sql.Date;

public class AttivitaDTO {
	protected int ID_Attivita;
	protected String codiceFiscaleCol;
	protected Date giornoInizio;
	protected Date giornoFine;
	protected String stato;
	
	public AttivitaDTO() {
		
    }
	
	public AttivitaDTO(int ID_Attivita, String codiceFiscaleCol) {
		this.ID_Attivita=ID_Attivita;
		this.codiceFiscaleCol=codiceFiscaleCol;
	}
	
	public AttivitaDTO(int ID_Attivita, String codiceFiscaleCol, Date giornoInizio, Date giornoFine) {
        this.ID_Attivita = ID_Attivita;
        this.codiceFiscaleCol = codiceFiscaleCol;
        this.giornoInizio = giornoInizio;
        this.giornoFine = giornoFine;
    }
	
	
	public int getID_Attivita() {
		return ID_Attivita;
	}

	public void setID_Attivita(int iD_Attivita) {
		ID_Attivita = iD_Attivita;
	}

	public String getCodiceFiscaleCol() {
		return codiceFiscaleCol;
	}

	public void setCodiceFiscaleCol(String codiceFiscaleCol) {
		this.codiceFiscaleCol = codiceFiscaleCol;
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
	
	
	
	
	
	
}
