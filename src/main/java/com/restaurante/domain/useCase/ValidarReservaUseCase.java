package com.restaurante.domain.useCase;

import com.restaurante.domain.dto.ReservaDTO;

public interface ValidarReservaUseCase {
    void execute(ReservaDTO dto);
}