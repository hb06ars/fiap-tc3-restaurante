package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.useCase.BuscarMesaDisponivelUseCase;
import com.restaurante.infra.exceptions.ReservaException;
import com.restaurante.infra.repository.postgres.MesaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class BuscarMesaDisponivelUseCaseImpl implements BuscarMesaDisponivelUseCase {

    @Autowired
    MesaRepository mesaRepository;

    @Override
    public MesaDisponivelDTO execute(Long restauranteId, LocalDateTime dataReserva) {
        List<Object[]> mesasDisponiveis = mesaRepository.buscarMesasDisponiveis(restauranteId, dataReserva);
        if (mesasDisponiveis != null && !mesasDisponiveis.isEmpty()) {
            return mesasDisponiveis.stream()
                    .map(result -> new MesaDisponivelDTO(
                            (Long) result[0],
                            (String) result[1],
                            (String) result[2]
                    ))
                    .findFirst().orElse(null);
        }
        throw new ReservaException("Não há mesas disponíveis no momento");
    }
}
