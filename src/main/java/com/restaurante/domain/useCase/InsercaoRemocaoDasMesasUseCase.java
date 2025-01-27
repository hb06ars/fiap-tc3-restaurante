package com.restaurante.domain.useCase;

import com.restaurante.domain.entity.ReservaEntity;

public interface InsercaoRemocaoDasMesasUseCase {
    void execute(Long idRestaurante, Integer capacidadeAtualRestaurante, Integer capacidadeAtualizada);
}