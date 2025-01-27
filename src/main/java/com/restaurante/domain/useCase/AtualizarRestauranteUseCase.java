package com.restaurante.domain.useCase;

import com.restaurante.domain.dto.RestauranteDTO;

public interface AtualizarRestauranteUseCase {
    RestauranteDTO execute(Long id, RestauranteDTO dto);
}
