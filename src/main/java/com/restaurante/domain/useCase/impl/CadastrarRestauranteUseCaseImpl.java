package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.useCase.CadastrarRestauranteUseCase;
import com.restaurante.domain.useCase.InserirRemoverMesasUseCase;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CadastrarRestauranteUseCaseImpl implements CadastrarRestauranteUseCase {

    private final RestauranteService service;
    private final FuncionamentoService funcionamentoService;
    private final InserirRemoverMesasUseCase insercaoRemocaoDasMesasUseCase;

    public CadastrarRestauranteUseCaseImpl(RestauranteService service, FuncionamentoService funcionamentoService,
                                           InserirRemoverMesasUseCase insercaoRemocaoDasMesasUseCase) {
        this.service = service;
        this.funcionamentoService = funcionamentoService;
        this.insercaoRemocaoDasMesasUseCase = insercaoRemocaoDasMesasUseCase;
    }

    @Override
    public RestauranteDTO execute(RestauranteDTO dto) {
        var restauranteExistente = service.restauranteJaExiste(dto.getNome(), dto.getLocalizacao());
        if (!restauranteExistente) {
            var restauranteCadastrado = service.save(dto);
            insercaoRemocaoDasMesasUseCase.execute(restauranteCadastrado.getId(), 0,
                    restauranteCadastrado.getCapacidade());
            funcionamentoService.inserirDataFuncionamentoInicial(restauranteCadastrado.getId());
            return restauranteCadastrado;
        } else {
            throw new RuntimeException("O Restaurante já existe, tente atualizar o mesmo!");
        }
    }
}
