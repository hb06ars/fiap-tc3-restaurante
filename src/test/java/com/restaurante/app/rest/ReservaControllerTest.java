package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.useCase.ReservarMesaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ReservaControllerTest {

    @Mock
    private ReservarMesaUseCase reservarMesaUseCase;

    @Mock
    private ReservaService reservaService;

    @InjectMocks
    private ReservaController reservaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCadastro() {
        ReservaDTO mockDto = new ReservaDTO();
        when(reservarMesaUseCase.salvar(any())).thenReturn(mockDto);

        ResponseEntity<ReservaDTO> response = reservaController.cadastro(mockDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDto, response.getBody());
    }

    @Test
    void testAtualizacao() {
        Long id = 1L;
        ReservaDTO mockDto = new ReservaDTO();
        when(reservarMesaUseCase.atualizar(eq(id), any())).thenReturn(mockDto);

        ResponseEntity<ReservaDTO> response = reservaController.atualizacao(id, mockDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockDto, response.getBody());
    }

    @Test
    void testBuscar() {
        Long idRestaurante = 1L;
        ReservaDTO mockDto = new ReservaDTO();
        when(reservaService.buscar(eq(idRestaurante), any())).thenReturn(Collections.singletonList(mockDto));

        ResponseEntity<List<ReservaDTO>> response = reservaController.buscar(idRestaurante, mockDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(mockDto, response.getBody().get(0));
    }
}
