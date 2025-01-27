package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    @Query(value = """
                SELECT r FROM ReservaEntity r
                WHERE r.id = :restauranteId
                AND (:statusReserva = '' OR r.statusReserva = :statusReserva)
                AND (:statusPagamento = '' OR r.statusPagamento = :statusPagamento)
                AND (:dataReserva BETWEEN r.dataDaReserva AND r.dataFimReserva) 
            """)
    List<ReservaEntity> findAllByFilter(
            @Param("restauranteId") Long restauranteId,
            @Param("statusReserva") String statusReserva,
            @Param("statusPagamento") String statusPagamento,
            @Param("dataReserva") LocalDateTime dataReserva
    );
}
