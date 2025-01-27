package com.restaurante.domain.entity;

import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.enums.DiaEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "funcionamento")
public class FuncionamentoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "O dia da semana não pode ser nulo. Por favor, forneça um valor o dia da semana.")
    private DiaEnum diaEnum;

    @Column(nullable = false)
    @NotNull(message = "A abertura não pode ser nula. Por favor, forneça um valor para a abertura.")
    private LocalTime abertura;

    @Column(nullable = false)
    @NotNull(message = "O fechamento não pode ser nulo. Por favor, forneça um valor para o fechamento.")
    private LocalTime fechamento;

    @Column(name = "restaurante_id", nullable = false)
    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante.")
    private Long restauranteId;

    public FuncionamentoEntity(FuncionamentoDTO dto) {
        this.id = dto.getId();
        this.diaEnum = dto.getDiaEnum();
        this.abertura = dto.getAbertura();
        this.fechamento = dto.getFechamento();
        this.restauranteId = dto.getRestauranteId();
    }
}
