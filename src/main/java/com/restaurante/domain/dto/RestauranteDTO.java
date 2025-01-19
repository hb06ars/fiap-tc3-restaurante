package com.restaurante.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurante.domain.enums.TipoCozinha;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestauranteDTO implements Serializable {

    private Long id;

    @NotBlank(message = "O nome do restaurante não pode ser vazio ou nulo")
    private String nome;

    @NotBlank(message = "A localização do restaurante não pode ser vazio ou nulo")
    private String localizacao;

    @NotBlank(message = "A localização do restaurante não pode ser vazio ou nulo")
    private TipoCozinha tipoCozinha;

}