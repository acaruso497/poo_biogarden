package dto;

import java.sql.Date;

public class IrrigazioneDTO extends AttivitaDTO{
	private String tipoIrrigazione;
	
	public IrrigazioneDTO() {
		super();
	}

	public IrrigazioneDTO(Date giornoInizio, Date giornoFine, String tipoIrrigazione) {
        super();
        this.giornoInizio = giornoInizio;
        this.giornoFine = giornoFine;
        this.tipoIrrigazione = tipoIrrigazione;
    }
	
	public IrrigazioneDTO(int ID_Attivita, String codiceFiscaleCol, Date giornoInizio, Date giornoFine, String tipoIrrigazione) {
        super(ID_Attivita, codiceFiscaleCol, giornoInizio, giornoFine);
        this.tipoIrrigazione = tipoIrrigazione;
    }
	
	public IrrigazioneDTO(String stato) {
        super();
        this.stato = stato;
    }


	public String getTipoIrrigazione() {
		return tipoIrrigazione;
	}

	public void setTipoIrrigazione(String tipoIrrigazione) {
		this.tipoIrrigazione = tipoIrrigazione;
	}

	
	

	
	
}
