package dto;

import java.sql.Date;

public class SeminaDTO extends AttivitaDTO{
	private Date giornoInizio;
	private Date giornoFine;
	private String tipoSemina;
	private String stato;
	
	public SeminaDTO() {}

    public SeminaDTO(Date giornoInizio, Date giornoFine, String tipoSemina) {
        this.giornoInizio = giornoInizio;
        this.giornoFine = giornoFine;
        this.tipoSemina = tipoSemina;
    }

	
	public SeminaDTO(int ID_Attivita, String codiceFiscaleCol, Date giornoInizio, Date giornoFine, String tipoSemina) {
		super(ID_Attivita, codiceFiscaleCol);
		this.giornoInizio=giornoInizio;
		this.giornoFine=giornoFine;
		this.tipoSemina=tipoSemina;
	}
	
	public SeminaDTO(String stato) {
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

	public String getTipoSemina() {
		return tipoSemina;
	}

	public void setTipoSemina(String tipoSemina) {
		this.tipoSemina = tipoSemina;
	}

	public String getStato() {
		return stato;
	}

	public void setStato(String stato) {
		this.stato = stato;
	}
	
	
	
}
