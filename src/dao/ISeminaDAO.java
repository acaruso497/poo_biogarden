package dao;

import dto.ProgettoColtivazioneDTO;
import dto.SeminaDTO;

public interface ISeminaDAO {
    
    void popolaSemina(ProgettoColtivazioneDTO progetto, SeminaDTO semina);
    
}