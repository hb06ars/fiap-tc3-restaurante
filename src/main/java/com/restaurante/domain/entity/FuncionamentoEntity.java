package com.restaurante.domain.entity;

import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.enums.DiaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @SequenceGenerator(name = "funcionamento_id_seq", sequenceName = "funcionamento_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funcionamento_id_seq")
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
