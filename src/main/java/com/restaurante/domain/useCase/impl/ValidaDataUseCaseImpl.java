package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.domain.useCase.ValidaDataUseCase;
import com.restaurante.domain.util.BuscarDiaDaSemana;
import com.restaurante.domain.util.BuscarFeriadoNacional;
import com.restaurante.infra.exceptions.ReservaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class ValidaDataUseCaseImpl implements ValidaDataUseCase {

    @Autowired
    FuncionamentoService funcionamentoService;

    @Override
    public void execute(Long restauranteId, LocalDateTime dataReserva, LocalDate diaSelecionado) {
        var mesasDisponiveis = funcionamentoService.buscarMesasDisponiveis(restauranteId, dataReserva, buscaDiaDaSemana(diaSelecionado));
        if (mesasDisponiveis == null || mesasDisponiveis.isEmpty())
            throw new ReservaException("A data e horário estão inválidos.");
    }


    private DiaEnum buscaDiaDaSemana(LocalDate diaSelecionado) {
        if (BuscarFeriadoNacional.consultar(diaSelecionado))
            return DiaEnum.FERIADOS;
        return BuscarDiaDaSemana.consultar(diaSelecionado);
    }
}
