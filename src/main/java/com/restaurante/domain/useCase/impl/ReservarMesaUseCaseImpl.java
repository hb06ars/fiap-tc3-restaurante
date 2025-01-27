package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.useCase.ReservarMesaUseCase;
import com.restaurante.domain.useCase.ValidaDataUseCase;
import com.restaurante.domain.useCase.ValidarReservaUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReservarMesaUseCaseImpl implements ReservarMesaUseCase {

    @Value("${tolerancia-mesa}")
    private Integer toleranciaMesa;

    @Autowired
    ValidarReservaUseCase validarReservaUseCase;
    @Autowired
    ValidaDataUseCase validaDataUseCase;

    @Autowired
    ReservaService service;

    @Override
    public ReservaDTO salvar(ReservaEntity entity) {
        validacoes(entity);
        return service.save(entity);
    }


    @Override
    public ReservaDTO atualizar(Long id, ReservaEntity reservaAtualizada) {
        ReservaEntity reservaOriginal = service.findById(id);
        validacoes(reservaAtualizada);
        reservaOriginal.setMesaId(reservaAtualizada.getMesaId());
        reservaOriginal.setUsuarioId(reservaAtualizada.getUsuarioId());
        reservaOriginal.setRestauranteId(reservaAtualizada.getRestauranteId());
        reservaOriginal.setDataDaReserva(reservaAtualizada.getDataDaReserva());
        reservaOriginal.setDataFimReserva(reservaAtualizada.getDataFimReserva());
        reservaOriginal.setValorReserva(reservaAtualizada.getValorReserva());
        reservaOriginal.setStatusPagamento(reservaAtualizada.getStatusPagamento());
        reservaOriginal.setStatusReserva(reservaAtualizada.getStatusReserva());
        return service.save(reservaAtualizada);
    }

    private void validacoes(ReservaEntity entity) {
        validaDataUseCase.execute(entity.getRestauranteId(), entity.getDataDaReserva(), entity.getDataDaReserva().toLocalDate());
        validarReservaUseCase.execute(entity);
        preencherHorarioDeSaida(entity);
    }

    private void preencherHorarioDeSaida(ReservaEntity entity) {
        entity.setDataFimReserva(entity.getDataDaReserva().plusHours(toleranciaMesa));
    }


}
