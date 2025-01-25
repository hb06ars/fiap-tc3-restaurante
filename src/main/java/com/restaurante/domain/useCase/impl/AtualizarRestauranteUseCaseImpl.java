package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.useCase.AtualizarRestauranteUseCase;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AtualizarRestauranteUseCaseImpl implements AtualizarRestauranteUseCase {

    @Autowired
    RestauranteService service;

    @Override
    public RestauranteDTO execute(Long id, RestauranteEntity restauranteAtualizado) {
        RestauranteEntity restauranteOriginal = service.findById(id);
        if (restauranteOriginal != null) {
            restauranteOriginal.setCapacidade(restauranteAtualizado.getCapacidade());
            restauranteOriginal.setNome(restauranteAtualizado.getNome());
            restauranteOriginal.setLocalizacao(restauranteAtualizado.getLocalizacao());
            restauranteOriginal.setTipoCozinha(restauranteAtualizado.getTipoCozinha());
            return service.save(restauranteOriginal);
        }
        throw new ObjectNotFoundException("Restaurante não encontrado no sistema!");
    }
}
