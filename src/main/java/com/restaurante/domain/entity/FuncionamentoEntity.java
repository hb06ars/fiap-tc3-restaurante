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
    private DiaEnum diaEnum;

    @Column(nullable = false)
    private LocalTime abertura;

    @Column(nullable = false)
    private LocalTime fechamento;

    @Column(name = "restaurante_id", nullable = false)
    private Long restauranteId;

    public FuncionamentoEntity(FuncionamentoDTO dto) {
        this.id = dto.getId();
        this.diaEnum = dto.getDiaEnum();
        this.abertura = dto.getAbertura();
        this.fechamento = dto.getFechamento();
        this.restauranteId = dto.getRestauranteId();
    }
}
