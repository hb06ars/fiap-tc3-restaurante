package com.restaurante.domain.useCase;

import com.restaurante.domain.dto.MesaDisponivelDTO;

import java.time.LocalDateTime;

public interface BuscarMesaDisponivelUseCase {
    MesaDisponivelDTO execute(Long restauranteId, LocalDateTime dataReserva);
}
