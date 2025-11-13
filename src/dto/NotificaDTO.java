package dto;

import java.sql.Date;


import dao.NotificaDAO;

public class NotificaDTO {
	private String titolo;
	private String descrizione;
	private Date dataEvento;
	private String utentiTag;

	
	public NotificaDTO(String titolo, String descrizione, Date dataEvento, String utentiTag) {
		this.titolo=titolo;
		this.descrizione=descrizione;
		this.dataEvento=dataEvento;
		this.utentiTag=utentiTag;
	}
	public NotificaDTO(String titolo, String descrizione, Date dataEvento) {
		this.titolo=titolo;
		this.descrizione=descrizione;
		this.dataEvento=dataEvento;
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
	

	public static  boolean legginotifiche(ColtivatoreDTO coltivatore) {
        return NotificaDAO.segnaNotificheColtivatoreComeLette(coltivatore);
    }
	public static boolean checknotifiche(ColtivatoreDTO coltivatore) {
        return NotificaDAO.ciSonoNotificheNonLette(coltivatore);
    }
	public static String mostranotifiche(ColtivatoreDTO coltivatore) {
        return NotificaDAO.getNotificheNonLette(coltivatore);
    }
	
}