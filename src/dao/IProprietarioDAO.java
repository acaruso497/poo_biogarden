package dao;

import dto.ProprietarioDTO;
import dto.UtenteDTO;
import java.util.ArrayList;
import java.util.List;

public interface IProprietarioDAO {
    
    boolean authP(UtenteDTO user);
    
    ProprietarioDTO getProprietario(ProprietarioDTO credenziali);
    
    boolean registraP(ProprietarioDTO proprietario);
    
    boolean aggiungiL(ProprietarioDTO proprietario);
    
    ArrayList<String> popolaComboProprietari();
    
    String getDestinatariUsernamesByProprietario(ProprietarioDTO proprietario);
    
    ArrayList<String> getColtivatoriByProprietario(ProprietarioDTO proprietario);
    
    List<String> getLottiByProprietario(ProprietarioDTO proprietario);
    
    List<String> getProgettiByProprietario(ProprietarioDTO proprietario);
    
}
