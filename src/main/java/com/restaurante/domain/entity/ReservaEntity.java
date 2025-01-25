package com.restaurante.domain.entity;

import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reserva")
public class ReservaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "mesa_id", nullable = false)
    private Long mesaId;

    @Column(name = "restaurante_id", nullable = false)
    private Long restauranteId;

    @Column(name = "data_da_reserva", nullable = false)
    private LocalDateTime dataDaReserva;

    @Column(name = "data_fim_reserva", nullable = false)
    private LocalDateTime dataFimReserva;

    @Column(name = "valor_reserva", nullable = false, precision = 10, scale = 2)
    private BigDecimal valorReserva;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 50)
    private StatusPagamentoEnum statusPagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_reserva", nullable = false, length = 50)
    private StatusReservaEnum statusReserva;

}
