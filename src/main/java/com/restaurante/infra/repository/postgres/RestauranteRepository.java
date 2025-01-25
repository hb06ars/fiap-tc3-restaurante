package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.RestauranteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RestauranteRepository extends JpaRepository<RestauranteEntity, Long> {

    @Query("SELECT r FROM RestauranteEntity r " +
            "WHERE (:nome IS NULL OR :nome = '' OR LOWER(r.nome) LIKE LOWER(CONCAT('%', :nome, '%'))) " +
            "AND (:localizacao IS NULL OR :localizacao = '' OR LOWER(r.localizacao) LIKE LOWER(CONCAT('%', :localizacao, '%'))) " +
            "AND (:tipoCozinha IS NULL OR :tipoCozinha = '' OR LOWER(r.tipoCozinha) = LOWER(:tipoCozinha))")
    List<RestauranteEntity> buscarRestaurantes(
            @Param("nome") String nome,
            @Param("localizacao") String localizacao,
            @Param("tipoCozinha") String tipoCozinha
    );


}