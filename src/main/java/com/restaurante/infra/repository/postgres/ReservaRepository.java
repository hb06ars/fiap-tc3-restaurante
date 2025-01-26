package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    @Query(value = """
                SELECT r FROM ReservaEntity r
                WHERE r.restauranteId = :restauranteId
                AND (:statusReserva IS NULL OR r.statusReserva = :statusReserva)
                AND (:statusPagamento IS NULL OR r.statusPagamento = :statusPagamento)
                AND (:dataDaReserva IS NULL OR r.dataDaReserva <= :dataDaReserva)
                AND (:dataDaReserva IS NULL OR r.dataFimReserva >= :dataDaReserva)
            """)
    List<ReservaEntity> findAllByFilter(
            @Param("restauranteId") Long restauranteId,
            @Param("statusReserva") StatusReservaEnum statusReserva,
            @Param("statusPagamento") StatusPagamentoEnum statusPagamento,
            @Param("dataDaReserva") LocalDateTime dataDaReserva
    );
}