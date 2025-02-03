package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.mapper.MesaDisponivelMapper;
import com.restaurante.domain.useCase.BuscarMesaDisponivelUseCase;
import com.restaurante.domain.util.DataFormat;
import com.restaurante.infra.exceptions.ReservaException;
import com.restaurante.infra.repository.postgres.MesaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class BuscarMesaDisponivelUseCaseImpl implements BuscarMesaDisponivelUseCase {

    private final MesaRepository mesaRepository;

    public BuscarMesaDisponivelUseCaseImpl(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    @Override
    public MesaDisponivelDTO execute(Long id, LocalDateTime dataReserva) {
        List<Object[]> mesasDisponiveis = mesaRepository.buscarMesasDisponiveis(id, DataFormat.truncate(dataReserva));
        List<MesaDisponivelDTO> mesasDisponivelDTO = MesaDisponivelMapper.convert(mesasDisponiveis);
        if (mesasDisponivelDTO != null && !mesasDisponivelDTO.isEmpty()) {
            return mesasDisponivelDTO.get(0);
        }
        throw new ReservaException("Não há mesas disponíveis no momento");
    }


}
