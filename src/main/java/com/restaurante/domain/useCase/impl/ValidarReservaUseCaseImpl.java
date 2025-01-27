package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.useCase.BuscarMesaDisponivelUseCase;
import com.restaurante.domain.useCase.ValidarReservaUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ValidarReservaUseCaseImpl implements ValidarReservaUseCase {

    private final BuscarMesaDisponivelUseCase buscarMesaDisponivelUseCase;

    public ValidarReservaUseCaseImpl(BuscarMesaDisponivelUseCase buscarMesaDisponivelUseCase) {
        this.buscarMesaDisponivelUseCase = buscarMesaDisponivelUseCase;
    }

    @Override
    public void execute(ReservaDTO dto) {
        MesaDisponivelDTO mesaDisponivelDTO = buscarMesaDisponivelUseCase.execute(dto.getRestauranteId(), dto.getDataDaReserva());
        dto.setMesaId(mesaDisponivelDTO.getMesaId());
    }
}
