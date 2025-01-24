package com.restaurante.domain.enums;

import lombok.Getter;

@Getter
public enum StatusReservaEnum {
    OCUPADO("Ocupado"),
    CANCELADO("Cancelado"),
    RESERVADO("Reservado");

    private final String descricao;

    StatusReservaEnum(String descricao) {
        this.descricao = descricao;
    }

}
