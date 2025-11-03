package dto;

public class ColturaDTO {
	private int ID_Coltura;
	private String varieta;
	private int raccoltoProdotto;
	private int max;
	private int min;
	private int avg;
	private int counter;
	
	
	public ColturaDTO(int ID_Coltura, String varieta) {
		this.ID_Coltura=ID_Coltura;
		this.varieta=varieta;
	}


	//Getter e setter
	public int getID_Coltura() { return ID_Coltura; }
	public void setID_Coltura(int iD_Coltura) { ID_Coltura = iD_Coltura; }

	public String getVarieta() { return varieta; }
	public void setVarieta(String varieta) { this.varieta = varieta; }
	
}
