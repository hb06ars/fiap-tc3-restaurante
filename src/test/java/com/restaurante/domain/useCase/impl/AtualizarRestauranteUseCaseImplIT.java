package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.useCase.InserirRemoverMesasUseCase;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class AtualizarRestauranteUseCaseImplIT extends BaseUnitTest {

    @Autowired
    private AtualizarRestauranteUseCaseImpl atualizarRestauranteUseCase;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private InserirRemoverMesasUseCase insercaoRemocaoDasMesasUseCase;

    @Test
    void testExecuteSuccess() {
        RestauranteDTO dto = getRandom(RestauranteDTO.class);
        dto.setCapacidade(3);
        var restauranteSaved = restauranteService.save(dto);
        RestauranteDTO result = atualizarRestauranteUseCase.execute(restauranteSaved.getId(), restauranteSaved);
        assertNotNull(result);
    }

    @Test
    void testExecuteRestauranteNotFound() {
        RestauranteDTO dto = getRandom(RestauranteDTO.class);
        dto.setCapacidade(3);
        assertThrows(ObjectNotFoundException.class, () -> atualizarRestauranteUseCase.execute(1L, dto));
    }
}
