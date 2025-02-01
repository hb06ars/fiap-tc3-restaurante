package com.restaurante.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurante.app.rest.request.RestauranteRequest;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.TipoCozinhaEnum;
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
    private TipoCozinhaEnum tipoCozinha;
    private Integer capacidade;

    public RestauranteDTO(RestauranteEntity restauranteEntity) {
        this.id = restauranteEntity.getId();
        this.nome = restauranteEntity.getNome();
        this.localizacao = restauranteEntity.getLocalizacao();
        this.tipoCozinha = restauranteEntity.getTipoCozinha();
        this.capacidade = restauranteEntity.getCapacidade();
    }

    public RestauranteDTO(RestauranteRequest request) {
        this.id = request.getId();
        this.nome = request.getNome();
        this.localizacao = request.getLocalizacao();
        this.tipoCozinha = request.getTipoCozinha();
        this.capacidade = request.getCapacidade();
    }
}