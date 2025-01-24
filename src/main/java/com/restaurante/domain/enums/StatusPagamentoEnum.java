package com.restaurante.domain.enums;

import lombok.Getter;

@Getter
public enum StatusPagamentoEnum {
    PAGO("Pago"),
    PENDENTE("Pendente pagamento"),
    CANCELADO("Pagamento Cancelado");

    private final String descricao;

    StatusPagamentoEnum(String descricao) {
        this.descricao = descricao;
    }

}
