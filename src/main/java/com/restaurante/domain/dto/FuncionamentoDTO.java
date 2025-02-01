
package com.restaurante.domain.dto;

import com.restaurante.app.rest.request.FuncionamentoRequest;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.enums.DiaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FuncionamentoDTO implements Serializable {

    private Long id;
    private DiaEnum diaEnum;
    private LocalTime abertura;
    private LocalTime fechamento;
    private Long restauranteId;

    public FuncionamentoDTO(FuncionamentoEntity entity) {
        this.id = entity.getId();
        this.diaEnum = entity.getDiaEnum();
        this.abertura = entity.getAbertura();
        this.fechamento = entity.getFechamento();
        this.restauranteId = entity.getRestauranteId();
    }

    public FuncionamentoDTO(FuncionamentoRequest request) {
        this.id = request.getId();
        this.diaEnum = request.getDiaEnum();
        this.abertura = request.getAbertura();
        this.fechamento = request.getFechamento();
        this.restauranteId = request.getRestauranteId();
    }
}