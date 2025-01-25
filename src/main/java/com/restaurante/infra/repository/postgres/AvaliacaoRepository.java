package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.AvaliacaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvaliacaoRepository extends JpaRepository<AvaliacaoEntity, Long> {
}