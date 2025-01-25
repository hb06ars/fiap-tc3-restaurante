package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.FuncionamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionamentoRepository extends JpaRepository<FuncionamentoEntity, Long> {
}