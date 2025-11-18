package dao;

import dto.*;

public interface IAttivitaDAO{
    boolean aggiornaStato(SeminaDTO semina, IrrigazioneDTO irrigazione, RaccoltaDTO raccolta, LottoDTO lotto);
}