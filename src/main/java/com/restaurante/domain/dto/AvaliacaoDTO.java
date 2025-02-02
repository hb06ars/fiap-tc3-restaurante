package com.restaurante.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.restaurante.app.rest.request.AvaliacaoRequest;
import com.restaurante.domain.entity.AvaliacaoEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    private LocalDateTime datapost;

    public AvaliacaoDTO(AvaliacaoEntity entity) {
        this.id = entity.getId();
        this.nota = entity.getNota();
        this.comentario = entity.getComentario();
        this.usuarioId = entity.getUsuarioId();
        this.restauranteId = entity.getRestauranteId();
        this.datapost = entity.getDatapost();
    }

    public AvaliacaoDTO(AvaliacaoRequest request) {
        this.id = request.getId();
        this.nota = request.getNota();
        this.comentario = request.getComentario();
        this.usuarioId = request.getUsuarioId();
        this.restauranteId = request.getRestauranteId();
        this.datapost = request.getDatapost();
    }
}