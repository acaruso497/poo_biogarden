package dto;

public class Coltivatore extends utente{
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String usernameProprietario;
	
	public Coltivatore(String username, String password, String nome, String cognome, 
					   String codiceFiscale, String usernameProprietario)  {
		super(username, password);
		this.nome=nome;
		this.cognome=cognome;
		this.codiceFiscale=codiceFiscale;
		this.usernameProprietario=usernameProprietario;
	}
	
	//Getter e Setter
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome;}

	public String getCognome() { return cognome; }
	public void setCognome(String cognome) { this.cognome = cognome; }

	public String getCodiceFiscale() { return codiceFiscale; }
	public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

	public String getUsernameProprietario() { return usernameProprietario; }
	public void setUsernameProprietario(String usernameProprietario) { this.usernameProprietario = usernameProprietario; }
	
	
}
