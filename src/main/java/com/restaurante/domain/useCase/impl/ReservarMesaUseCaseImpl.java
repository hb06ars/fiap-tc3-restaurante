package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.useCase.ValidarReservaUseCase;
import com.restaurante.domain.useCase.ReservarMesaUseCase;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReservarMesaUseCaseImpl implements ReservarMesaUseCase {

    @Autowired
    ValidarReservaUseCase validarReservaUseCase;

    @Override
    public ReservaDTO execute(ReservaEntity entity) {
        validarReservaUseCase.execute(entity.getRestauranteId(), entity.getDataDaReserva(), entity.getMesaId());


        throw new ObjectNotFoundException("Restaurante não encontrado no sistema!");
    }


}
