package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.MesaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MesaRepository extends JpaRepository<MesaEntity, Long> {
}