package com.restaurante.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MesaDisponivelDTO {
    private Long mesaId;
    private String mesaNome;

}
