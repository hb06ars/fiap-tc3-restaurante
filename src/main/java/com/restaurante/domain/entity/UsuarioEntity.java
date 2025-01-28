package com.restaurante.domain.entity;

import com.restaurante.domain.dto.UsuarioDTO;
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
@Builder
@Entity
@Table(name = "usuario")
public class UsuarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotNull(message = "O nome não pode ser nulo. Por favor, forneça um valor.")
    private String nome;

    @Column(nullable = true, length = 255, unique = true)
    @NotNull(message = "O email não pode ser nulo. Por favor, forneça um valor.")
    private String email;

    @Column(nullable = true, length = 50, unique = true)
    @NotNull(message = "O celular não pode ser nulo. Por favor, forneça um valor.")
    private String celular;

    public UsuarioEntity(Long id, String nome, String email, String celular) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.celular = celular;
    }

    public UsuarioEntity(UsuarioDTO dto) {
        this.id = dto.getId();
        this.nome = dto.getNome();
        this.email = dto.getEmail();
        this.celular = dto.getCelular();
    }
}
