package dao;

import dto.IrrigazioneDTO;
import dto.ProgettoColtivazioneDTO;

public interface IIrrigazioneDAO {
    
    void popolaIrrigazione(ProgettoColtivazioneDTO progetto, IrrigazioneDTO irrigazione);
    
}
