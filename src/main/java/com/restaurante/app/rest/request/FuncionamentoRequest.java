package com.restaurante.app.rest.request;

import com.restaurante.domain.enums.DiaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuncionamentoRequest {

    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O dia da semana não pode ser nulo. Por favor, forneça um valor o dia da semana.")
    private DiaEnum diaEnum;

    @NotNull(message = "A abertura não pode ser nula. Por favor, forneça um valor para a abertura.")
    private LocalTime abertura;

    @NotNull(message = "O fechamento não pode ser nulo. Por favor, forneça um valor para o fechamento.")
    private LocalTime fechamento;

    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante.")
    private Long restauranteId;

}
