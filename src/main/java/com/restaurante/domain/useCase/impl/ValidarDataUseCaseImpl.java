package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.domain.useCase.ValidarDataUseCase;
import com.restaurante.domain.util.BuscarDiaDaSemana;
import com.restaurante.domain.util.BuscarFeriadoNacional;
import com.restaurante.infra.exceptions.ReservaException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@Slf4j
public class ValidarDataUseCaseImpl implements ValidarDataUseCase {

    private final FuncionamentoService funcionamentoService;

    public ValidarDataUseCaseImpl(FuncionamentoService funcionamentoService) {
        this.funcionamentoService = funcionamentoService;
    }

    @Override
    public void execute(Long restauranteId, LocalDateTime dataReserva, LocalDate diaSelecionado) {
        var funcionamentoDisponivel = funcionamentoService.validarDataFuncionamento(restauranteId,
                dataReserva, buscaDiaDaSemana(diaSelecionado));
        if (funcionamentoDisponivel == null)
            throw new ReservaException("A data e horário estão inválidos. Verifique o horário de funcionamento" +
                    " e se há data de funcionamento cadastrado.");
    }


    public DiaEnum buscaDiaDaSemana(LocalDate diaSelecionado) {
        if (BuscarFeriadoNacional.consultar(diaSelecionado))
            return DiaEnum.FERIADOS;
        return BuscarDiaDaSemana.consultar(diaSelecionado);
    }
}
