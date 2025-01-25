package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {


    boolean validarDataReserva(Long restauranteId, LocalDateTime dataReserva, Long mesaId);
}