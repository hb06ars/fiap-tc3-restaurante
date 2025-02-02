package com.restaurante.app.rest.request;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservaFilter {

    private Long id;

    private LocalDateTime dataDaReserva = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private StatusPagamentoEnum statusPagamento = StatusPagamentoEnum.PENDENTE;

    @Enumerated(EnumType.STRING)
    private StatusReservaEnum statusReserva = StatusReservaEnum.OCUPADO;

    public ReservaFilter(ReservaDTO dto) {
        this.id = dto.getId();
        this.dataDaReserva = dto.getDataDaReserva() != null ? dto.getDataDaReserva() : LocalDateTime.now();
        this.statusPagamento = dto.getStatusPagamento() != null ? dto.getStatusPagamento() : StatusPagamentoEnum.PENDENTE;
        this.statusReserva = dto.getStatusReserva() != null ? dto.getStatusReserva() : StatusReservaEnum.OCUPADO;
    }

}
