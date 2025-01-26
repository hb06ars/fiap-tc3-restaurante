package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.useCase.ValidaDataUseCase;
import com.restaurante.infra.exceptions.ReservaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ValidaDataUseCaseImpl implements ValidaDataUseCase {

    @Override
    public void execute(LocalDateTime dataReserva) {
        throw new ReservaException("A data e horário estão inválidos.");
    }
}
