
package com.restaurante.domain.dto;

import com.restaurante.app.rest.request.UsuarioRequest;
import com.restaurante.domain.entity.UsuarioEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UsuarioDTO {

    private Long id;
    private String nome;
    private String email;
    private String celular;

    public UsuarioDTO(UsuarioEntity usuarioEntity) {
        this.id = usuarioEntity.getId();
        this.nome = usuarioEntity.getNome();
        this.email = usuarioEntity.getEmail();
        this.celular = usuarioEntity.getCelular();
    }

    public UsuarioDTO(UsuarioRequest request) {
        this.id = request.getId();
        this.nome = request.getNome();
        this.email = request.getEmail();
        this.celular = request.getCelular();
    }
}