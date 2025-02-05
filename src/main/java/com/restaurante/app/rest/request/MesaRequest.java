package com.restaurante.app.rest.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class MesaRequest {
    private Long id;

    @NotNull(message = "O nome da mesa não pode ser nulo. Por favor, forneça um valor " +
            "para o nome da mesa, exemplo: Mesa 1.")
    private String nomeMesa;

    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor " +
            "para o restaurante.")
    private Long restauranteId;

}
