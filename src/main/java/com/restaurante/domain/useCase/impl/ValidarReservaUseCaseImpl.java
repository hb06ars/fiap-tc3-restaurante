package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.useCase.ValidarReservaUseCase;
import com.restaurante.infra.repository.postgres.ReservaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ValidarReservaUseCaseImpl implements ValidarReservaUseCase {

    @Autowired
    ReservaRepository repository;

    @Override
    public boolean execute(Long restauranteId, LocalDateTime dataReserva) {
        if (repository.validarDataReserva(restauranteId, dataReserva).isEmpty()) {

        }

        return false;
    }
}
