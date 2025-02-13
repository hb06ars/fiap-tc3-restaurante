package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.FuncionamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface FuncionamentoRepository extends JpaRepository<FuncionamentoEntity, Long> {

    @Query(value = """
                SELECT * FROM funcionamento 
                WHERE restaurante_id = :restauranteId 
                AND dia_enum = :diaEnum 
                AND :dataReserva BETWEEN abertura AND fechamento ;
            """, nativeQuery = true)
    List<FuncionamentoEntity> validarData(
            @Param("restauranteId") Long restauranteId,
            @Param("dataReserva") LocalTime dataReserva,
            @Param("diaEnum") String diaEnum
    );

    List<FuncionamentoEntity> findAllByRestauranteId(Long idRestaurante);
}