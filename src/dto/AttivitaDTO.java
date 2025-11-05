package dto;


public class AttivitaDTO {
	int ID_Attivita;
	String codiceFiscaleCol;
	
	public AttivitaDTO(int ID_Attivita, String codiceFiscaleCol) {
		this.ID_Attivita=ID_Attivita;
		this.codiceFiscaleCol=codiceFiscaleCol;
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
	
	
	
}
