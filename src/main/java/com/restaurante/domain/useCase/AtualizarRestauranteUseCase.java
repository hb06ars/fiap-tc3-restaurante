package com.restaurante.domain.useCase;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;

public interface AtualizarRestauranteUseCase {
    RestauranteDTO execute(Long id, RestauranteEntity restauranteEntity);
}
