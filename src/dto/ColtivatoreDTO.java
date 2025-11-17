package dto;

import dao.ColtivatoreDAO;

public class ColtivatoreDTO extends UtenteDTO{
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String usernameProprietario;
	private String esperienza;
	
	public ColtivatoreDTO(String username, String password, String nome, String cognome, 
					   String codiceFiscale, String usernameProprietario, String esperienza)  {
		super(username, password);
		this.nome=nome;
		this.cognome=cognome;
		this.codiceFiscale=codiceFiscale;
		this.usernameProprietario=usernameProprietario;
		this.esperienza=esperienza;
	}
	public ColtivatoreDTO(String username, String password, String nome, String cognome, 
			   String codiceFiscale, String usernameProprietario)  {
		super(username, password);
		this.nome=nome;
		this.cognome=cognome;
		this.codiceFiscale=codiceFiscale;
		this.usernameProprietario=usernameProprietario;
		
}
	public ColtivatoreDTO(String username) {
		super(username);
	}
	public ColtivatoreDTO(String username, String password) {
		super(username, password);
	}
	public ColtivatoreDTO() {
		super(null);
	}	
	
	//Getter e Setter
	public String getEsperienza() { return esperienza; }
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome;}

	public String getCognome() { return cognome; }
	public void setCognome(String cognome) { this.cognome = cognome; }

	public String getCodiceFiscale() { return codiceFiscale; }
	public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }

	public String getUsernameProprietario() { return usernameProprietario; }
	public void setUsernameProprietario(String usernameProprietario) { this.usernameProprietario = usernameProprietario; }
}
