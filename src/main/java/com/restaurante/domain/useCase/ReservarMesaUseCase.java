package com.restaurante.domain.useCase;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;

public interface ReservarMesaUseCase {
    ReservaDTO salvar(ReservaEntity entity);

    ReservaDTO atualizar(Long id, ReservaEntity entity);
}
