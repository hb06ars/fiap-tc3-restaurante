package com.restaurante.app.rest.request;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.enums.TipoCozinhaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestauranteRequest {

    private Long id;

    @NotNull(message = "O nome não pode ser nulo. Por favor, forneça um valor.")
    private String nome;

    @NotNull(message = "A localização não pode ser nula. Por favor, forneça um valor.")
    private String localizacao;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de cozinha não pode ser nula. Por favor, forneça um valor.")
    private TipoCozinhaEnum tipoCozinha;

    @Min(0)
    @NotNull(message = "O tipo de cozinha não pode ser nula. Por favor, forneça um valor.")
    private Integer capacidade = 0;

}
