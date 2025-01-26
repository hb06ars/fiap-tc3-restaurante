package com.restaurante.domain.useCase;

import com.restaurante.domain.entity.ReservaEntity;

public interface ValidarReservaUseCase {
    void execute(ReservaEntity reservaEntity);
}