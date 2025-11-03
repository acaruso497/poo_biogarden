package dto;

import java.sql.Date;

public class ProgettoColtivazioneDTO {
	private int ID_Progetto;
	private String titolo;
	private String descrizione;
	private double stimaRaccolto;
	private Date dataInizio;
	private Date dataFine;
	private int idLotto;
	private boolean done;
	
	public ProgettoColtivazioneDTO(int ID_Progetto, String titolo, String descrizione, double stimaRaccolto, Date dataInizio, Date dataFine, int idLotto, boolean done)  {
		this.ID_Progetto=ID_Progetto;
		this.titolo=titolo;
		this.descrizione=descrizione;
		this.stimaRaccolto=stimaRaccolto;
		this.dataInizio=dataInizio;
		this.dataFine=dataFine;
		this.idLotto=idLotto;
		this.done=done;
	}

	//Getter e setter
	public int getID_Progetto() { return ID_Progetto; }
	public void setID_Progetto(int iD_Progetto) { ID_Progetto = iD_Progetto; }

	public String getTitolo() { return titolo; }
	public void setTitolo(String titolo) { this.titolo = titolo; }

	public String getDescrizione() { return descrizione; }
	public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

	public double getStimaRaccolto() { return stimaRaccolto; }
	public void setStimaRaccolto(double stimaRaccolto) { this.stimaRaccolto = stimaRaccolto; }

	public Date getDataInizio() { return dataInizio; }
	public void setDataInizio(Date dataInizio) { this.dataInizio = dataInizio; }

	public Date getDataFine() { return dataFine; }
	public void setDataFine(Date dataFine) { this.dataFine = dataFine; }

	public int getIdLotto() { return idLotto; }
	public void setIdLotto(int idLotto) { this.idLotto = idLotto; }

	public boolean isDone() { return done; }
	public void setDone(boolean done) { this.done = done; }
	
	
}
