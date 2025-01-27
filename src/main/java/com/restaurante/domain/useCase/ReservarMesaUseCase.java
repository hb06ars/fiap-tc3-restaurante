package com.restaurante.domain.useCase;

import com.restaurante.domain.dto.ReservaDTO;

public interface ReservarMesaUseCase {
    ReservaDTO salvar(ReservaDTO dto);

    ReservaDTO atualizar(Long id, ReservaDTO dto);
}
