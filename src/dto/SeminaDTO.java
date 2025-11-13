package dto;

import java.sql.Date;

public class SeminaDTO extends AttivitaDTO{
	private String tipoSemina;
	
	public SeminaDTO() {}

    public SeminaDTO(Date giornoInizio, Date giornoFine, String tipoSemina) {
        this.giornoInizio = giornoInizio;
        this.giornoFine = giornoFine;
        this.tipoSemina = tipoSemina;
    }

	
    public SeminaDTO(int ID_Attivita, String codiceFiscaleCol, Date giornoInizio, Date giornoFine, String tipoSemina) {
        super(ID_Attivita, codiceFiscaleCol, giornoInizio, giornoFine);
        this.tipoSemina = tipoSemina;
    }
	
    public SeminaDTO(String stato) {
        super();
        this.stato = stato;
    }


	public String getTipoSemina() {
		return tipoSemina;
	}

	public void setTipoSemina(String tipoSemina) {
		this.tipoSemina = tipoSemina;
	}

	
	
	
}
