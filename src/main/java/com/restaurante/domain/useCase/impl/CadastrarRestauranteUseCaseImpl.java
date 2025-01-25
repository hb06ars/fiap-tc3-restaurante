package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.useCase.CadastrarRestauranteUseCase;
import com.restaurante.infra.exceptions.RecordAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CadastrarRestauranteUseCaseImpl implements CadastrarRestauranteUseCase {

    @Autowired
    RestauranteService service;

    @Override
    public RestauranteDTO execute(RestauranteEntity entity) {
        if (!service.restauranteJaExiste(entity.getNome(), entity.getLocalizacao()))
            return service.save(entity);
        throw new RecordAlreadyExistsException("Este restaurante já existe no sistema!");
    }
}
