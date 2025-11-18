package dao;

import dto.ColtivatoreDTO;
import dto.ColturaDTO;
import dto.IrrigazioneDTO;
import dto.LottoDTO;
import dto.ProgettoColtivazioneDTO;
import dto.ProprietarioDTO;
import dto.RaccoltaDTO;
import dto.SeminaDTO;
import java.util.ArrayList;

public interface IProgettoColtivazioneDAO {
    
    boolean registraProgetto(ProgettoColtivazioneDTO progetto, LottoDTO lotto, ArrayList<ColturaDTO> coltureDTOList);
    
    boolean insertAttivita(SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta, LottoDTO lotto, ProgettoColtivazioneDTO progetto);
    
    boolean controlloProgettoChiuso(LottoDTO lotto);
    
    ArrayList<String> dateI_FProgCB(String titolo_progetto, ColtivatoreDTO coltivatore);
    
    boolean terminaProgetto(ProgettoColtivazioneDTO progetto, LottoDTO lotto);
    
    void popolaDatiProgetto(ProgettoColtivazioneDTO progetto);
    
    boolean isCompletata(ProprietarioDTO proprietario, ProgettoColtivazioneDTO progetto);
    
}