package com.restaurante.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurante.app.rest.request.MesaRequest;
import com.restaurante.domain.entity.MesaEntity;
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
public class MesaDTO implements Serializable {

    private Long id;
    private String nomeMesa;
    private Long restauranteId;

    public MesaDTO(MesaEntity entity) {
        this.id = entity.getId();
        this.nomeMesa = entity.getNomeMesa();
        this.restauranteId = entity.getRestauranteId();
    }

    public MesaDTO(MesaRequest request) {
        this.id = request.getId();
        this.nomeMesa = request.getNomeMesa();
        this.restauranteId = request.getRestauranteId();
    }
}