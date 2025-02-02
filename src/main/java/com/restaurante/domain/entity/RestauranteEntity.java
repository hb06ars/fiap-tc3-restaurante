package com.restaurante.domain.entity;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.enums.TipoCozinhaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "restaurante")
public class RestauranteEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotNull(message = "O nome não pode ser nulo. Por favor, forneça um valor.")
    private String nome;

    @Column(nullable = false, length = 255)
    @NotNull(message = "A localização não pode ser nula. Por favor, forneça um valor.")
    private String localizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50, name = "tipocozinha")
    @NotNull(message = "O tipo de cozinha não pode ser nula. Por favor, forneça um valor.")
    private TipoCozinhaEnum tipoCozinha;

    @Column(nullable = false)
    @Min(0)
    @NotNull(message = "O tipo de cozinha não pode ser nula. Por favor, forneça um valor.")
    private Integer capacidade = 0;

    public RestauranteEntity(RestauranteDTO dto) {
        this.id = dto.getId();
        this.nome = dto.getNome();
        this.localizacao = dto.getLocalizacao();
        this.tipoCozinha = dto.getTipoCozinha();
        this.capacidade = dto.getCapacidade();
    }
}
