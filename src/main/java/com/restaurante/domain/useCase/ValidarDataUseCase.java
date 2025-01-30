package com.restaurante.domain.useCase;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface ValidarDataUseCase {
    void execute(Long restauranteId, LocalDateTime dataReserva, LocalDate diaSelecionado);
}
