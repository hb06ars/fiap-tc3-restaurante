package com.restaurante.domain.enums;

import lombok.Getter;

@Getter
public enum DiaEnum {
    SEGUNDA("Segunda-Feira"),
    TERCA("Terça-Feira"),
    QUARTA("Quarta-Feira"),
    QUINTA("Quinta-Feira"),
    SEXTA("Sexta-Feira"),
    SABADO("Sábado"),
    DOMINGO("Domingo"),
    FERIADOS("Feriado");

    private final String descricao;

    DiaEnum(String descricao) {
        this.descricao = descricao;
    }

}
