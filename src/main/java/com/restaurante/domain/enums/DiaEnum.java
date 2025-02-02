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
            1, DOMINGO,
            2, SEGUNDA,
            3, TERCA,
            4, QUARTA,
            5, QUINTA,
            6, SEXTA,
            7, SABADO
    );

    public static DiaEnum fromInt(int dia) {
        return MAPA_DIAS.getOrDefault(dia, FERIADOS);
    }
}
