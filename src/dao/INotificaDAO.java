package dao;


import dto.ColtivatoreDTO;
import dto.NotificaDTO;

public interface INotificaDAO {
    
    boolean Inserisci_NotificaDB(NotificaDTO notifica);
    
    boolean ciSonoNotificheNonLette(ColtivatoreDTO coltivatore);
    
    boolean segnaNotificheColtivatoreComeLette(ColtivatoreDTO coltivatore);
    
    String getNotificheNonLette(ColtivatoreDTO coltivatore);
    
}
