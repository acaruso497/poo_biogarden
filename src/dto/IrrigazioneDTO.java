package dto;

import java.sql.Date;

public class IrrigazioneDTO extends AttivitaDTO{
	private Date giornoInizio;
	private Date giornoFine;
	private String tipoIrrigazione;
	private String stato;
	
	public IrrigazioneDTO() {}

    public IrrigazioneDTO(Date giornoInizio, Date giornoFine, String tipoIrrigazione) {
        this.giornoInizio = giornoInizio;
        this.giornoFine = giornoFine;
        this.tipoIrrigazione = tipoIrrigazione;
    }
	
	public IrrigazioneDTO(int ID_Attivita, String codiceFiscaleCol, Date giornoInizio, Date giornoFine, String tipoIrrigazione) {
		super(ID_Attivita, codiceFiscaleCol);
		this.giornoInizio=giornoInizio;
		this.giornoFine=giornoFine;
		this.tipoIrrigazione=tipoIrrigazione;
	}
	
	public IrrigazioneDTO(String stato) {
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

	public String getTipoIrrigazione() {
		return tipoIrrigazione;
	}

	public void setTipoIrrigazione(String tipoIrrigazione) {
		this.tipoIrrigazione = tipoIrrigazione;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
	
	

	
	
}
