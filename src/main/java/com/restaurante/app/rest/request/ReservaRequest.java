package com.restaurante.app.rest.request;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaRequest {

    private Long id;

    @NotNull(message = "O usuário não pode ser nulo. Por favor, forneça um valor para o usuário.")
    private Long usuarioId;

    @NotNull(message = "A mesa não pode ser nula. Por favor, forneça um valor para a mesa.")
    private Long mesaId;

    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante.")
    private Long restauranteId;

    @NotNull(message = "A data da reserva não pode ser nula. Por favor, forneça um valor para a data da reserva.")
    private LocalDateTime dataDaReserva = LocalDateTime.now();

    @NotNull(message = "A data fim reserva não pode ser nula. Por favor, forneça um valor para a data fim.")
    private LocalDateTime dataFimReserva;

    @NotNull(message = "O valor da reserva não pode ser nulo. Por favor, forneça um valor.")
    private BigDecimal valorReserva = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status do pagamento não pode ser nulo. Por favor, forneça um valor.")
    private StatusPagamentoEnum statusPagamento = StatusPagamentoEnum.PENDENTE;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status da reserva não pode ser nulo. Por favor, forneça um valor.")
    private StatusReservaEnum statusReserva = StatusReservaEnum.OCUPADO;

    public ReservaRequest(ReservaDTO dto) {
        this.id = dto.getId();
        this.usuarioId = dto.getUsuarioId();
        this.mesaId = dto.getMesaId();
        this.restauranteId = dto.getRestauranteId();
        this.dataDaReserva = dto.getDataDaReserva() != null ? dto.getDataDaReserva() : LocalDateTime.now();
        this.dataFimReserva = dto.getDataFimReserva();
        this.valorReserva = dto.getValorReserva() != null ? dto.getValorReserva() : BigDecimal.ZERO;
        this.statusPagamento = dto.getStatusPagamento() != null ? dto.getStatusPagamento() : StatusPagamentoEnum.PENDENTE;
        this.statusReserva = dto.getStatusReserva() != null ? dto.getStatusReserva() : StatusReservaEnum.OCUPADO;
    }

}
