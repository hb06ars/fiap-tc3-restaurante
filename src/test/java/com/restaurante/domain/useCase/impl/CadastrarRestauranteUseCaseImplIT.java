package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.RestauranteDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class CadastrarRestauranteUseCaseImplIT {

    @Autowired
    private CadastrarRestauranteUseCaseImpl cadastrarRestauranteUseCase;

    @Test
    void executeDeveRetornarMesaDisponivelDTO() {
        RestauranteDTO dto = getRandom(RestauranteDTO.class);
        dto.setCapacidade(3);
        RestauranteDTO resultado = cadastrarRestauranteUseCase.execute(dto);
        assertNotNull(resultado);
    }

    @Test
    void executeDeveLancarReservaExceptionQuandoNaoHouverMesasDisponiveis() {
        RestauranteDTO dto = getRandom(RestauranteDTO.class);
        dto.setCapacidade(3);
        RestauranteDTO resultado = cadastrarRestauranteUseCase.execute(dto);
        assertThrows(RuntimeException.class, () -> cadastrarRestauranteUseCase.execute(resultado));
    }
}
