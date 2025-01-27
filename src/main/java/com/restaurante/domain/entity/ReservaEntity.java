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
import javax.validation.constraints.NotNull;
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
    @NotNull(message = "O usuário não pode ser nulo. Por favor, forneça um valor para o usuário.")
    private Long usuarioId;

    @Column(name = "mesa_id", nullable = false)
    @NotNull(message = "A mesa não pode ser nula. Por favor, forneça um valor para a mesa.")
    private Long mesaId;

    @Column(name = "restaurante_id", nullable = false)
    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante.")
    private Long restauranteId;

    @Column(name = "data_da_reserva", nullable = false)
    @NotNull(message = "A data da reserva não pode ser nula. Por favor, forneça um valor para a data da reserva.")
    private LocalDateTime dataDaReserva = LocalDateTime.now();

    @Column(name = "data_fim_reserva", nullable = false)
    @NotNull(message = "A data fim reserva não pode ser nula. Por favor, forneça um valor para a data fim.")
    private LocalDateTime dataFimReserva;

    @Column(name = "valor_reserva", nullable = false, precision = 10, scale = 2)
    @NotNull(message = "O valor da reserva não pode ser nulo. Por favor, forneça um valor.")
    private BigDecimal valorReserva = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_pagamento", nullable = false, length = 50)
    @NotNull(message = "O status do pagamento não pode ser nulo. Por favor, forneça um valor.")
    private StatusPagamentoEnum statusPagamento = StatusPagamentoEnum.PENDENTE;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_reserva", nullable = false, length = 50)
    @NotNull(message = "O status da reserva não pode ser nulo. Por favor, forneça um valor.")
    private StatusReservaEnum statusReserva = StatusReservaEnum.OCUPADO;

}
