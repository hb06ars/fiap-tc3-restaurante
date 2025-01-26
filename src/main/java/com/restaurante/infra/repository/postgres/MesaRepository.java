package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.entity.MesaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface MesaRepository extends JpaRepository<MesaEntity, Long> {

    @Query(value = """
                SELECT 
                    m.id AS mesaId,
                    m.nome_mesa AS mesaNome
                FROM mesa m
                LEFT JOIN restaurante r ON m.restaurante_id = r.id
                LEFT JOIN reserva res 
                    ON res.mesa_id = m.id 
                    AND :dataReserva BETWEEN res.data_da_reserva AND res.data_fim_reserva
                WHERE r.id = :restauranteId AND res.id IS NULL
                GROUP BY m.id, m.nome_mesa
            """, nativeQuery = true)
    List<MesaDisponivelDTO> buscarMesasDisponiveis(
            @Param("restauranteId") Long restauranteId,
            @Param("dataReserva") LocalDateTime dataReserva
    );
}
