package com.restaurante.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurante.domain.entity.AvaliacaoEntity;
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
public class AvaliacaoDTO implements Serializable {

    private Long id;
    private Integer nota;
    private String comentario;
    private Long usuarioId;
    private Long restauranteId;

    public AvaliacaoDTO(AvaliacaoEntity entity) {
        this.id = entity.getId();
        this.nota = entity.getNota();
        this.comentario = entity.getComentario();
        this.usuarioId = entity.getUsuarioId();
        this.restauranteId = entity.getRestauranteId();
    }
}