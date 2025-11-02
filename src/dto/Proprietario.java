package dto;

public class Proprietario extends utente {
	private String nome;
	private String cognome;
	private String codiceFiscale;
	
	public Proprietario(String username, String password, String nome, String cognome, String codiceFiscale) {
		super(username, password);
		this.nome=nome;
		this.cognome=cognome;
		this.codiceFiscale=codiceFiscale;
	}
	
	public Proprietario(String username) {
		super(username);
	}

	//Getter e Setter
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome;}

	public String getCognome() { return cognome; }
	public void setCognome(String cognome) { this.cognome = cognome; }

	public String getCodiceFiscale() { return codiceFiscale; }
	public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }
	
}
