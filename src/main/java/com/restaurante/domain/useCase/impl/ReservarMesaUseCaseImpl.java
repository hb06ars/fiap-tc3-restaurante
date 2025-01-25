package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.useCase.ReservarMesaUseCase;
import com.restaurante.domain.useCase.ValidarReservaUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReservarMesaUseCaseImpl implements ReservarMesaUseCase {

    @Autowired
    ValidarReservaUseCase validarReservaUseCase;

    @Autowired
    ReservaService service;

    @Override
    public ReservaDTO execute(ReservaEntity entity) {

        validarReservaUseCase.execute(entity.getRestauranteId(), entity.getDataDaReserva());


        return service.save(entity);
    }


}
