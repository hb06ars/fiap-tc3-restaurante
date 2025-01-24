package com.restaurante.app.rest;

import com.restaurante.domain.dto.MessageSuccessDTO;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.enums.TipoCozinha;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ControllerTest {
    @InjectMocks
    Controller tarifaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTarifa() {
        ResponseEntity<RestauranteDTO> result = tarifaController.cadastro();
        RestauranteDTO restauranteDTO = new RestauranteDTO();
        restauranteDTO.setId(1L);
        restauranteDTO.setNome("La Brasa");
        restauranteDTO.setLocalizacao("Av. Paulista, 1000");
        restauranteDTO.setTipoCozinha(TipoCozinha.MEXICANA);
        Assertions.assertEquals(new ResponseEntity<RestauranteDTO>(restauranteDTO, null, HttpStatus.OK).getStatusCode(), result.getStatusCode());
    }
}

