package com.restaurante.domain.entity;

import com.restaurante.domain.dto.AvaliacaoDTO;
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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "avaliacao")
public class AvaliacaoEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @Max(10)
    @Column(nullable = false)
    @NotNull(message = "A nota não pode ser nula. Por favor, forneça um valor para a nota.")
    private Integer nota;

    @Column(length = 500)
    private String comentario;

    @Column(nullable = false)
    @NotNull(message = "O usuário não pode ser nulo. Por favor, forneça um valor para o usuário.")
    private Long usuarioId;

    @Column(nullable = false)
    @NotNull(message = "O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante.")
    private Long restauranteId;

    @Column(name = "datapost", nullable = false)
    @NotNull(message = "O data do post não pode ser nulo. Por favor, forneça uma data.")
    private LocalDateTime datapost = LocalDateTime.now();

    public AvaliacaoEntity(AvaliacaoDTO dto) {
        this.id = dto.getId();
        this.nota = dto.getNota();
        this.comentario = dto.getComentario();
        this.usuarioId = dto.getUsuarioId();
        this.restauranteId = dto.getRestauranteId();
        this.datapost = dto.getDatapost() != null ? dto.getDatapost() : LocalDateTime.now();
    }
}
