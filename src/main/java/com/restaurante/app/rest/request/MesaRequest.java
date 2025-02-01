package com.restaurante.app.rest.request;

import com.restaurante.domain.dto.MesaDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@RequiredArgsConstructor
public class MesaRequest {
    private Long id;

    @NotNull(message = "O nome da mesa não pode ser nulo. Por favor, forneça um valor para o nome da mesa, exemplo: Mesa 1.")
    private String nomeMesa;

    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante.")
    private Long restauranteId;

}
