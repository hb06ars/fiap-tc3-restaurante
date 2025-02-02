package com.restaurante.domain.enums;

import java.util.Map;

public enum DiaEnum {
    DOMINGO("Domingo"),
    SEGUNDA("Segunda-Feira"),
    TERCA("Terça-Feira"),
    QUARTA("Quarta-Feira"),
    QUINTA("Quinta-Feira"),
    SEXTA("Sexta-Feira"),
    SABADO("Sábado"),
    FERIADOS("Feriado");

    private final String descricao;

    DiaEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    private static final Map<Integer, DiaEnum> MAPA_DIAS = Map.of(
            1, SEGUNDA,
            2, TERCA,
            3, QUARTA,
            4, QUINTA,
            5, SEXTA,
            6, SABADO,
            7, DOMINGO
    );

    public static DiaEnum fromInt(int dia) {
        return MAPA_DIAS.getOrDefault(dia, FERIADOS);
    }
}
