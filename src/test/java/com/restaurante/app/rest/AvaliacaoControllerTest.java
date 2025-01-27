package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.AvaliacaoService;
import com.restaurante.domain.dto.AvaliacaoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AvaliacaoControllerTest {

    @Mock
    private AvaliacaoService service;

    @InjectMocks
    private AvaliacaoController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste para avaliar")
    void testAvaliar() {
        AvaliacaoDTO inputDto = new AvaliacaoDTO();
        inputDto.setRestauranteId(1L);
        when(service.save(any(AvaliacaoDTO.class))).thenReturn(inputDto);

        ResponseEntity<AvaliacaoDTO> response = controller.avaliar(inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inputDto, response.getBody());
        verify(service, times(1)).save(any(AvaliacaoDTO.class));
    }

    @Test
    @DisplayName("Teste para buscar avaliações por restaurante")
    void testBuscar() {
        Long idRestaurante = 1L;
        AvaliacaoDTO avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setRestauranteId(idRestaurante);
        List<AvaliacaoDTO> expectedList = Arrays.asList(avaliacaoDTO);

        when(service.listarPorRestaurante(idRestaurante)).thenReturn(expectedList);

        ResponseEntity<List<AvaliacaoDTO>> response = controller.buscar(idRestaurante);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(service, times(1)).listarPorRestaurante(idRestaurante);
    }
}
