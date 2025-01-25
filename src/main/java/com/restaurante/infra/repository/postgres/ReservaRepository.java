package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    @Query("SELECT r FROM ReservaEntity r " +
            "WHERE r.restauranteId = :restauranteId " +
            "AND :dataReserva BETWEEN r.dataDaReserva AND r.dataFimReserva")
    List<ReservaEntity> validarDataReserva(
            @Param("restauranteId") Long restauranteId,
            @Param("dataReserva") LocalDateTime dataReserva
    );


}