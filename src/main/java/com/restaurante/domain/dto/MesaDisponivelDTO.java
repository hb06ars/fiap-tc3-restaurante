package com.restaurante.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MesaDisponivelDTO {
    private Long mesaId;
    private String mesaNome;
    private String statusMesa;
}
