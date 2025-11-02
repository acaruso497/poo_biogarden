package dto;

import java.sql.Date;

public class Notifica {
	private String titolo;
	private String descrizione;
	private Date dataEvento;
	private String utentiTag;
	
	public Notifica(String titolo, String descrizione, Date dataEvento, String utentiTag) {
		this.titolo=titolo;
		this.descrizione=descrizione;
		this.dataEvento=dataEvento;
		this.utentiTag=utentiTag;
	}

	//Getter e setter
	public String getTitolo() { return titolo; }
	public void setTitolo(String titolo) { this.titolo = titolo; }

	public String getDescrizione() { return descrizione; }
	public void setDescrizione(String descrizione) { this.descrizione = descrizione; }

	public Date getDataEvento() { return dataEvento; }
	public void setDataEvento(Date dataEvento) { this.dataEvento = dataEvento; }

	public String getUtentiTag() { return utentiTag; }
	public void setUtentiTag(String utentiTag) { this.utentiTag = utentiTag; }
	
	
}
