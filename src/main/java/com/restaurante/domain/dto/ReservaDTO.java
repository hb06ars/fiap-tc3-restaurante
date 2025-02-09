
package com.restaurante.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.restaurante.app.rest.request.ReservaFilter;
import com.restaurante.app.rest.request.ReservaRequest;
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataDaReserva;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dataFimReserva;
    private BigDecimal valorReserva;
    private StatusPagamentoEnum statusPagamento;
    private StatusReservaEnum statusReserva;

    public ReservaDTO(ReservaEntity entity) {
        this.id = entity.getId();
        this.usuarioId = entity.getUsuarioId();
        this.mesaId = entity.getMesaId();
        this.restauranteId = entity.getRestauranteId();
        this.dataDaReserva = entity.getDataDaReserva();
        this.dataFimReserva = entity.getDataFimReserva();
        this.valorReserva = entity.getValorReserva();
        this.statusPagamento = entity.getStatusPagamento();
        this.statusReserva = entity.getStatusReserva();
    }

    public ReservaDTO(ReservaRequest request) {
        this.id = request.getId();
        this.usuarioId = request.getUsuarioId();
        this.mesaId = request.getMesaId();
        this.restauranteId = request.getRestauranteId();
        this.dataDaReserva = request.getDataDaReserva();
        this.dataFimReserva = request.getDataFimReserva();
        this.valorReserva = request.getValorReserva();
        this.statusPagamento = request.getStatusPagamento();
        this.statusReserva = request.getStatusReserva();
    }

    public ReservaDTO(ReservaFilter request) {
        this.id = request.getId();
        this.dataDaReserva = request.getDataDaReserva();
        this.statusPagamento = request.getStatusPagamento();
        this.statusReserva = request.getStatusReserva();
    }
}