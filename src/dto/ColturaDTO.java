package dto;

public class ColturaDTO {
	private int ID_Coltura;
	private String varieta;
	private int raccoltoProdotto;
	private int max;
	private int min;
	private int avg;
	private int counter;
	
	//prelevo dati dal database (utilizzo questo costruttore)
	public ColturaDTO(int ID_Coltura, String varieta, int raccoltoProdotto, int max, int min, int avg, int counter) {
		this.ID_Coltura=ID_Coltura;
		this.varieta=varieta;
		this.raccoltoProdotto=raccoltoProdotto;
		this.max=max;
		this.min=min;
		this.avg=avg;
		this.counter=counter;
	}
	
	public ColturaDTO(String varieta) {
		this.varieta=varieta;
	}


	//Getter e setter
	public int getID_Coltura() { return ID_Coltura; }
	public void setID_Coltura(int iD_Coltura) { ID_Coltura = iD_Coltura; }

	public String getVarieta() { return varieta; }
	public void setVarieta(String varieta) { this.varieta = varieta; }


	public int getRaccoltoProdotto() {
		return raccoltoProdotto;
	}


	public void setRaccoltoProdotto(int raccoltoProdotto) {
		this.raccoltoProdotto = raccoltoProdotto;
	}


	public int getMax() {
		return max;
	}


	public void setMax(int max) {
		this.max = max;
	}


	public int getMin() {
		return min;
	}


	public void setMin(int min) {
		this.min = min;
	}


	public int getAvg() {
		return avg;
	}


	public void setAvg(int avg) {
		this.avg = avg;
	}


	public int getCounter() {
		return counter;
	}


	public void setCounter(int counter) {
		this.counter = counter;
	}
	
	
	
}
