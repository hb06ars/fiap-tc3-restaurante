package com.restaurante.domain.useCase;

import java.time.LocalDateTime;

public interface ValidaDataUseCase {
    void execute(LocalDateTime dataReserva);
}
