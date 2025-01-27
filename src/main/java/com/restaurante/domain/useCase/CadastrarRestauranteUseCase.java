package com.restaurante.domain.useCase;

import com.restaurante.domain.dto.RestauranteDTO;

public interface CadastrarRestauranteUseCase {
    RestauranteDTO execute(RestauranteDTO dto);
}
