package dto;

import dao.ProprietarioDAO;

public class ProprietarioDTO extends UtenteDTO{
	private String nome;
	private String cognome;
	private String codiceFiscale;
	
	public ProprietarioDTO(String username, String password, String nome, String cognome, String codiceFiscale) {
		super(username, password);
		this.nome=nome;
		this.cognome=cognome;
		this.codiceFiscale=codiceFiscale;
	}
	
	public ProprietarioDTO(String username) {
		super(username);
	}
	
	public ProprietarioDTO(String username, String password) {
		super(username, password);
	}

	@Override
	public boolean autentica() {
		return ProprietarioDAO.authP(this);
	}
	
	//Getter e Setter
	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome;}

	public String getCognome() { return cognome; }
	public void setCognome(String cognome) { this.cognome = cognome; }

	public String getCodiceFiscale() { return codiceFiscale; }
	public void setCodiceFiscale(String codiceFiscale) { this.codiceFiscale = codiceFiscale; }
	
}
