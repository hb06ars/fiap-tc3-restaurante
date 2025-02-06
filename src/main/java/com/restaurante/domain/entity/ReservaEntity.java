package com.restaurante.domain.entity;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "reserva")
public class ReservaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @SequenceGenerator(name = "reserva_id_seq", sequenceName = "reserva_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reserva_id_seq")
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

    @Column(name = "data_fim_reserva")
    @NotNull(message = "A data fim reserva não pode ser nula. Por favor, forneça um valor para a data fim.")
    private LocalDateTime dataFimReserva = LocalDateTime.now().plusHours(2);

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

    public ReservaEntity(ReservaDTO dto) {
        this.id = dto.getId();
        this.usuarioId = dto.getUsuarioId();
        this.mesaId = dto.getMesaId();
        this.restauranteId = dto.getRestauranteId();
        this.dataDaReserva = dto.getDataDaReserva() != null ? dto.getDataDaReserva() : LocalDateTime.now();
        this.dataFimReserva = dto.getDataFimReserva();
        this.valorReserva = dto.getValorReserva() != null ? dto.getValorReserva() : BigDecimal.ZERO;
        this.statusPagamento = dto.getStatusPagamento() != null ? dto.getStatusPagamento()
                : StatusPagamentoEnum.PENDENTE;
        this.statusReserva = dto.getStatusReserva() != null ? dto.getStatusReserva() : StatusReservaEnum.OCUPADO;
    }

}
