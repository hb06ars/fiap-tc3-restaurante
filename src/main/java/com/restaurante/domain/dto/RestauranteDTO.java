package com.restaurante.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.TipoCozinha;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    private String nome;
    private String localizacao;
    private TipoCozinha tipoCozinha;

    public RestauranteDTO(RestauranteEntity restauranteEntity) {
        this.id = restauranteEntity.getId();
        this.nome = restauranteEntity.getNome();
        this.localizacao = restauranteEntity.getLocalizacao();
        this.tipoCozinha = restauranteEntity.getTipoCozinha();
    }
}