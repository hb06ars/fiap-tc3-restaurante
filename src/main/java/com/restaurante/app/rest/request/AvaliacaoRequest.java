package com.restaurante.app.rest.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AvaliacaoRequest {
    private Long id;

    @Min(0)
    @Max(10)
    @NotNull(message = "A nota não pode ser nula. Por favor, forneça um valor para a nota.")
    private Integer nota;

    private String comentario;

    @NotNull(message = "O usuário não pode ser nulo. Por favor, forneça um valor para o usuário.")
    private Long usuarioId;

    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante.")
    private Long restauranteId;

    @NotNull(message = "O data do post não pode ser nulo. Por favor, forneça uma data.")
    private LocalDateTime datapost = LocalDateTime.now();

}
