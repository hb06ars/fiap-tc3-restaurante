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
@Table(name = "usuario")
public class UsuarioEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotNull(message = "O nome não pode ser nulo. Por favor, forneça um valor.")
    private String nome;

    @Column(nullable = true, length = 255)
    @NotNull(message = "O email não pode ser nulo. Por favor, forneça um valor.")
    private String email;

    @Column(nullable = true, length = 50)
    @NotNull(message = "O celular não pode ser nulo. Por favor, forneça um valor.")
    private String celular;

    public UsuarioEntity(String nome, String email, String celular) {
        this.nome = nome;
        this.email = email;
        this.celular = celular;
    }
}
