package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.useCase.AtualizarRestauranteUseCase;
import com.restaurante.domain.useCase.InserirRemoverMesasUseCase;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AtualizarRestauranteUseCaseImpl implements AtualizarRestauranteUseCase {

    private final RestauranteService service;
    private final InserirRemoverMesasUseCase addRemoveMesas;

    public AtualizarRestauranteUseCaseImpl(RestauranteService service, InserirRemoverMesasUseCase addRemoveMesas) {
        this.service = service;
        this.addRemoveMesas = addRemoveMesas;
    }

    @Override
    public RestauranteDTO execute(Long id, RestauranteDTO dto) {
        RestauranteDTO original = service.findById(id);
        if (original != null) {
            addRemoveMesas.execute(original.getId(), original.getCapacidade(), dto.getCapacidade());
            return service.update(original.getId(), dto);
        }
        throw new ObjectNotFoundException("Restaurante não encontrado no sistema!");
    }
}
