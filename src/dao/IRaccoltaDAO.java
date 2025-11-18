package dao;

import dto.ColturaDTO;
import dto.ProgettoColtivazioneDTO;
import dto.RaccoltaDTO;

public interface IRaccoltaDAO {
    
    boolean sommaRaccolto(int raccolto, ColturaDTO coltura, ProgettoColtivazioneDTO progetto);
    
    void popolaRaccolta(ProgettoColtivazioneDTO progetto, RaccoltaDTO raccolta);
    
}
