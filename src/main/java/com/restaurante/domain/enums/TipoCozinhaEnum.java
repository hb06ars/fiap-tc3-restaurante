package com.restaurante.domain.enums;

import lombok.Getter;

@Getter
public enum TipoCozinhaEnum {
    GERAL("Geral"),
    ITALIANA("Italiana"),
    JAPONESA("Japonesa"),
    BRASILEIRA("Brasileira"),
    CHINESA("Chinesa"),
    MEXICANA("Mexicana"),
    INDIANA("Indiana"),
    FRANCESA("Francesa"),
    TAILANDESA("Tailandesa"),
    AMERICANA("Americana"),
    MEDITERRANEA("Mediterrânea"),
    VEGETARIANA("Vegetariana"),
    VEGANA("Vegana"),
    FAST_FOOD("Fast Food"),
    ARABE("Árabe"),
    COREANA("Coreana"),
    AFRICANA("Africana"),
    ESPANHOLA("Espanhola"),
    PORTUGUESA("Portuguesa"),
    ALEMA("Alemã"),
    RUSSA("Russa");

    private final String descricao;

    TipoCozinhaEnum(String descricao) {
        this.descricao = descricao;
    }

}
