package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.useCase.BuscarMesaDisponivelUseCase;
import com.restaurante.domain.useCase.ValidarReservaUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidarReservaUseCaseImpl implements ValidarReservaUseCase {

    @Autowired
    BuscarMesaDisponivelUseCase buscarMesaDisponivelUseCase;

    @Override
    public void execute(ReservaEntity reservaEntity) {
        MesaDisponivelDTO mesaDisponivelDTO = buscarMesaDisponivelUseCase.execute(reservaEntity.getRestauranteId(), reservaEntity.getDataDaReserva());
        reservaEntity.setMesaId(mesaDisponivelDTO.getMesaId());
    }
}
