package dto;

public abstract class UtenteDTO {

	private String username;
    private String password;
    
    public UtenteDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
    
    public UtenteDTO(String username) {
        this.username = username;
    }
    
    public abstract boolean autentica();
    
    // Getter e Setter
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
	
}
