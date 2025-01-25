package com.restaurante.domain.useCase;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;

public interface CadastrarRestauranteUseCase {
    RestauranteDTO execute(RestauranteEntity restauranteEntity);
}
