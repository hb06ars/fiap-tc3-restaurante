package com.restaurante.domain.useCase;

import java.time.LocalDateTime;

public interface ValidarReservaUseCase {
    boolean execute(Long restauranteId, LocalDateTime dataReserva);
}