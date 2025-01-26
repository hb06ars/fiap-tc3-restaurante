package com.restaurante.domain.util;

import com.restaurante.domain.enums.DiaEnum;

import java.time.LocalDate;

public class BuscarDiaDaSemana {
    public static DiaEnum consultar(LocalDate diaSelecionado) {
        return switch (diaSelecionado.getDayOfWeek()) {
            case MONDAY -> DiaEnum.SEGUNDA;
            case TUESDAY -> DiaEnum.TERCA;
            case WEDNESDAY -> DiaEnum.QUARTA;
            case THURSDAY -> DiaEnum.QUINTA;
            case FRIDAY -> DiaEnum.SEXTA;
            case SATURDAY -> DiaEnum.SABADO;
            case SUNDAY -> DiaEnum.DOMINGO;
        };
    }
}
