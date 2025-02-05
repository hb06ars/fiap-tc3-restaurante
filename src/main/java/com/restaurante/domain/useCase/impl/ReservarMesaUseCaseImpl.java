package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.useCase.ReservarMesaUseCase;
import com.restaurante.domain.useCase.ValidarDataUseCase;
import com.restaurante.domain.useCase.ValidarReservaUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReservarMesaUseCaseImpl implements ReservarMesaUseCase {

    private final Integer toleranciaMesa;
    private final ValidarReservaUseCase validarReservaUseCase;
    private final ValidarDataUseCase validaDataUseCase;
    private final ReservaService service;

    public ReservarMesaUseCaseImpl(@Value("${tolerancia-mesa}") Integer toleranciaMesa,
                                   ValidarReservaUseCase validarReservaUseCase, ValidarDataUseCase validaDataUseCase,
                                   ReservaService service) {
        this.toleranciaMesa = toleranciaMesa;
        this.validarReservaUseCase = validarReservaUseCase;
        this.validaDataUseCase = validaDataUseCase;
        this.service = service;
    }

    @Override
    public ReservaDTO salvar(ReservaDTO dto) {
        validacoes(dto);
        return service.save(dto);
    }


    @Override
    public ReservaDTO atualizar(Long id, ReservaDTO reservaAtualizada) {
        ReservaDTO reservaOriginal = service.findById(id);
        if (reservaOriginal.getDataDaReserva() != reservaAtualizada.getDataDaReserva())
            validacoes(reservaAtualizada);
        reservaOriginal.setDataDaReserva(reservaAtualizada.getDataDaReserva());
        reservaOriginal.setDataFimReserva(reservaAtualizada.getDataFimReserva());
        reservaOriginal.setValorReserva(reservaAtualizada.getValorReserva());
        reservaOriginal.setStatusPagamento(reservaAtualizada.getStatusPagamento());
        reservaOriginal.setStatusReserva(reservaAtualizada.getStatusReserva());
        return service.save(reservaOriginal);
    }

    private void validacoes(ReservaDTO dto) {
        validaDataUseCase.execute(dto.getRestauranteId(), dto.getDataDaReserva(),
                dto.getDataDaReserva().toLocalDate());
        validarReservaUseCase.execute(dto);
        preencherHorarioDeSaida(dto);
    }

    private void preencherHorarioDeSaida(ReservaDTO dto) {
        dto.setDataFimReserva(dto.getDataDaReserva().plusHours(toleranciaMesa));
    }


}
