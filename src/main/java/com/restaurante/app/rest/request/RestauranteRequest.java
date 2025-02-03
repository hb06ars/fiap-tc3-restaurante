package com.restaurante.app.rest.request;

import com.restaurante.domain.enums.TipoCozinhaEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
