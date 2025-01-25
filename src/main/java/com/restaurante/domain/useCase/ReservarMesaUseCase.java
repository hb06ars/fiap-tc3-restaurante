package com.restaurante.domain.useCase;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;

public interface ReservarMesaUseCase {
    ReservaDTO execute(ReservaEntity entity);
}
