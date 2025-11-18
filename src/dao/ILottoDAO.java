package dao;
import dto.ProgettoColtivazioneDTO;
import dto.ProprietarioDTO;

public interface ILottoDAO {
    
    String getLottoProgettoByProprietario(ProgettoColtivazioneDTO progetto, ProprietarioDTO proprietario);
    
}