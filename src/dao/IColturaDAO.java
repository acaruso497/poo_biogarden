package dao;

import dto.ColturaDTO;
import dto.LottoDTO;
import dto.ProgettoColtivazioneDTO;
import dto.ProprietarioDTO;
import java.util.ArrayList;
import java.util.List;

public interface IColturaDAO {
    
    String getRaccoltoProdotto(ProprietarioDTO proprietario, LottoDTO lotto, ArrayList<ColturaDTO> coltureList);
    
    ArrayList<String> getColtureProprietario(ProprietarioDTO proprietario, ProgettoColtivazioneDTO progetto);
    
    List<String> getColturaByLotto(LottoDTO lotto);
    
    long getNumeroRaccolte(ColturaDTO coltura);
    
    double getMediaRaccolto(ColturaDTO coltura);
    
    double getMinRaccolto(ColturaDTO coltura);
    
    double getMaxRaccolto(ColturaDTO coltura);
    
}
