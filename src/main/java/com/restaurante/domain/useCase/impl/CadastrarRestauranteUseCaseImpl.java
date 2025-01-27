package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.useCase.CadastrarRestauranteUseCase;
import com.restaurante.domain.useCase.InsercaoRemocaoDasMesasUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CadastrarRestauranteUseCaseImpl implements CadastrarRestauranteUseCase {

    @Autowired
    RestauranteService service;

    @Autowired
    InsercaoRemocaoDasMesasUseCase insercaoRemocaoDasMesasUseCase;

    @Override
    public RestauranteDTO execute(RestauranteEntity entity) {
        var restauranteExistente = service.restauranteJaExiste(entity.getNome(), entity.getLocalizacao());
        if (!restauranteExistente) {
            var restauranteCadastrado = service.save(entity);
            insercaoRemocaoDasMesasUseCase.execute(restauranteCadastrado.getId(), 0, restauranteCadastrado.getCapacidade());
            return restauranteCadastrado;
        } else {
            throw new RuntimeException("O Restaurante já existe, tente atualizar o mesmo!");
        }
    }
}
