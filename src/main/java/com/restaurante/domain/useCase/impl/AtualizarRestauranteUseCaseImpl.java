package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.useCase.AtualizarRestauranteUseCase;
import com.restaurante.domain.useCase.InsercaoRemocaoDasMesasUseCase;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AtualizarRestauranteUseCaseImpl implements AtualizarRestauranteUseCase {

    @Autowired
    RestauranteService service;

    @Autowired
    InsercaoRemocaoDasMesasUseCase insercaoRemocaoDasMesasUseCase;

    @Override
    public RestauranteDTO execute(Long id, RestauranteDTO dto) {
        RestauranteEntity restauranteOriginal = service.findById(id);
        if (restauranteOriginal != null) {
            insercaoRemocaoDasMesasUseCase.execute(restauranteOriginal.getId(), restauranteOriginal.getCapacidade(), dto.getCapacidade());
            return service.update(restauranteOriginal.getId(), dto);
        }
        throw new ObjectNotFoundException("Restaurante não encontrado no sistema!");
    }
}
