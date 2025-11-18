package dao;

import dto.ColtivatoreDTO;
import dto.UtenteDTO;
import java.util.List;

public interface IColtivatoreDAO {
    
    boolean authC(UtenteDTO user);
    
    boolean registraC(ColtivatoreDTO coltivatore);
    
    ColtivatoreDTO getColtivatore(ColtivatoreDTO credenziali);
    
    List<String> popolaProgettiCB(ColtivatoreDTO coltivatore);
    
    List<String> getTipiAttivitaColtivatore(ColtivatoreDTO coltivatore, String progetto);
    
    List<String> getIdAttivitaColtivatore(ColtivatoreDTO coltivatore, String progetto);
    
    String[] getDateByAttivitaId(String idAttivita, String tipoAttivita);
    
    String getLottoEPosizione(String progetto, ColtivatoreDTO coltivatore);
    
    String getStimaRaccolto(ColtivatoreDTO coltivatore, String progetto);
    
    String getIrrigazione(ColtivatoreDTO coltivatore, String progetto);
    
    String getTipoSemina(String idSemina);
    
    List<String> getColtura(ColtivatoreDTO coltivatore, String progetto);
    
    boolean usernameColtivatoreEsiste(ColtivatoreDTO coltivatore);
    
}
