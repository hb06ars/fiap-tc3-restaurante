package com.restaurante.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "mesa")
public class MesaEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_mesa", nullable = false, length = 100)
    @NotNull(message = "O nome da mesa não pode ser nulo. Por favor, forneça um valor para o nome da mesa, exemplo: Mesa 1.")
    private String nomeMesa;

    @Column(name = "restaurante_id", nullable = false)
    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante.")
    private Long restauranteId;

    public MesaEntity(String nomeMesa, Long restauranteId) {
        this.nomeMesa = nomeMesa;
        this.restauranteId = restauranteId;
    }
}
