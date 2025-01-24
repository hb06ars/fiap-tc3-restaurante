package com.restaurante.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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
    private String nomeMesa;

    @Column(name = "restaurante_id", nullable = false)
    private Long restauranteId;

    public MesaEntity(String nomeMesa, Long restauranteId) {
        this.nomeMesa = nomeMesa;
        this.restauranteId = restauranteId;
    }
}
