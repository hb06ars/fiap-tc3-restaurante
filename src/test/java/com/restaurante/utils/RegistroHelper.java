package com.restaurante.utils;

import com.restaurante.domain.entity.AvaliacaoEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.TipoCozinhaEnum;

import java.time.LocalDateTime;

public class RegistroHelper {

    public static UsuarioEntity gerarUsuario() {
        return UsuarioEntity.builder()
                .id(1L)
                .nome("Fulano de Tal")
                .email("email@email.com")
                .celular("11999999999")
                .build();
    }

    public static RestauranteEntity gerarRestaurante() {
        return RestauranteEntity.builder()
                .id(1L)
                .nome("La Bella Pizza")
                .tipoCozinha(TipoCozinhaEnum.ITALIANA)
                .localizacao("São Paulo")
                .capacidade(100)
                .build();
    }

    public static AvaliacaoEntity gerarAvaliacao() {
        return AvaliacaoEntity.builder()
                .id(1L)
                .nota(5)
                .comentario("Ótima comida!")
                .restauranteId(1L)
                .usuarioId(1L)
                .datapost(LocalDateTime.now())
                .build();
    }

}
