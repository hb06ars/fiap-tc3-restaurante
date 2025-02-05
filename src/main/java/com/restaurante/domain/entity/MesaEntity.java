package com.restaurante.domain.entity;

import com.restaurante.domain.dto.MesaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Builder
@Entity
@Table(name = "mesa")
public class MesaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_mesa", nullable = false, length = 100)
    @NotNull(message = "O nome da mesa não pode ser nulo. Por favor, forneça um valor para o " +
            "nome da mesa, exemplo: Mesa 1.")
    private String nomeMesa;

    @Column(name = "restaurante_id", nullable = false)
    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante.")
    private Long restauranteId;

    public MesaEntity(Long id, String nomeMesa, Long restauranteId) {
        this.id = id;
        this.nomeMesa = nomeMesa;
        this.restauranteId = restauranteId;
    }

    public MesaEntity(MesaDTO dto) {
        this.id = dto.getId();
        this.nomeMesa = dto.getNomeMesa();
        this.restauranteId = dto.getRestauranteId();
    }
}
