package com.restaurante.app.rest.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequest {
    private Long id;

    @NotNull(message = "O nome não pode ser nulo. Por favor, forneça um valor.")
    private String nome;

    @NotNull(message = "O email não pode ser nulo. Por favor, forneça um valor.")
    private String email;

    @NotNull(message = "O celular não pode ser nulo. Por favor, forneça um valor.")
    private String celular;

}
