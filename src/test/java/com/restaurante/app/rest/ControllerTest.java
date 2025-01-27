package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.enums.TipoCozinhaEnum;
import com.restaurante.domain.useCase.CadastrarRestauranteUseCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;

class ControllerTest {
    @InjectMocks
    RestauranteController tarifaController;

    @Mock
    RestauranteService restauranteService;

    @Mock
    CadastrarRestauranteUseCase cadastrarRestauranteUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTarifa() {
        RestauranteDTO dto = RestauranteDTO.builder()
                .nome("La Brasa")
                .tipoCozinha(TipoCozinhaEnum.BRASILEIRA)
                .localizacao("Av. Paulista 1000")
                .capacidade(100)
                .build();

        Assertions.assertNotNull(cadastrarRestauranteUseCase);
        Mockito.when(restauranteService.save(any())).thenReturn(dto);
        ResponseEntity<RestauranteDTO> result = tarifaController.cadastrar(dto);
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
    }
}

