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
    private final InserirRemoverMesasUseCase insercaoRemocaoDasMesasUseCase;

    public AtualizarRestauranteUseCaseImpl(RestauranteService service, InserirRemoverMesasUseCase insercaoRemocaoDasMesasUseCase) {
        this.service = service;
        this.insercaoRemocaoDasMesasUseCase = insercaoRemocaoDasMesasUseCase;
    }

    @Override
    public RestauranteDTO execute(Long id, RestauranteDTO dto) {
        RestauranteDTO restauranteOriginal = service.findById(id);
        if (restauranteOriginal != null) {
            insercaoRemocaoDasMesasUseCase.execute(restauranteOriginal.getId(), restauranteOriginal.getCapacidade(), dto.getCapacidade());
            return service.update(restauranteOriginal.getId(), dto);
        }
        throw new ObjectNotFoundException("Restaurante não encontrado no sistema!");
    }
}
