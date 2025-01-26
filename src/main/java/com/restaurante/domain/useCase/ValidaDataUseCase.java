package com.restaurante.domain.useCase;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ValidaDataUseCase {
    void execute(Long restauranteId, LocalDateTime dataReserva, LocalDate diaSelecionado);
}
