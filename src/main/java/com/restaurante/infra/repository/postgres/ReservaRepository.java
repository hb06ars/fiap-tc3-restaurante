package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.ReservaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {
}