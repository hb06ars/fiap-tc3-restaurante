
package com.restaurante.domain.dto;

import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO implements Serializable {

    private Long id;
    private Long usuarioId;
    private Long mesaId;
    private Long restauranteId;
    private LocalDateTime dataDaReserva;
    private BigDecimal valorReserva;
    private StatusPagamentoEnum statusPagamento;
    private StatusReservaEnum statusReserva;

    public ReservaDTO(ReservaEntity entity) {
        this.id = entity.getId();
        this.usuarioId = entity.getUsuarioId();
        this.mesaId = entity.getMesaId();
        this.restauranteId = entity.getRestauranteId();
        this.dataDaReserva = entity.getDataDaReserva();
        this.valorReserva = entity.getValorReserva();
        this.statusPagamento = entity.getStatusPagamento();
        this.statusReserva = entity.getStatusReserva();
    }

}