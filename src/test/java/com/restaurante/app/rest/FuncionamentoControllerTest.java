package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.dto.MessageSuccessDTO;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FuncionamentoControllerTest {

    @Mock
    private FuncionamentoService service;

    @InjectMocks
    private FuncionamentoController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste para cadastrar funcionamento")
    void testCadastro() {
        FuncionamentoDTO inputDto = new FuncionamentoDTO();
        inputDto.setId(1L);
        when(service.save(any(FuncionamentoDTO.class))).thenReturn(inputDto);

        ResponseEntity<FuncionamentoDTO> response = controller.cadastro(inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inputDto, response.getBody());
        verify(service, times(1)).save(any(FuncionamentoDTO.class));
    }

    @Test
    @DisplayName("Teste para atualizar funcionamento")
    void testAtualizar() {
        Long idFuncionamento = 1L;
        FuncionamentoDTO inputDto = new FuncionamentoDTO();
        inputDto.setId(idFuncionamento);
        when(service.update(anyLong(), any(FuncionamentoDTO.class))).thenReturn(inputDto);

        ResponseEntity<FuncionamentoDTO> response = controller.atualizar(idFuncionamento, inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inputDto, response.getBody());
        verify(service, times(1)).update(eq(idFuncionamento), any(FuncionamentoDTO.class));
    }

    @Test
    @DisplayName("Teste para deletar funcionamento")
    void testDeletar() {
        Long idFuncionamento = 1L;
        doNothing().when(service).delete(idFuncionamento);

        ResponseEntity<MessageSuccessDTO> response = controller.deletar(idFuncionamento);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registro removido com sucesso", response.getBody().getMensagem());
        verify(service, times(1)).delete(idFuncionamento);
    }

    @Test
    @DisplayName("Teste para buscar funcionamento por restaurante")
    void testBuscarPorRestaurante() {
        Long idRestaurante = 1L;
        FuncionamentoDTO funcionamentoDTO = new FuncionamentoDTO();
        funcionamentoDTO.setRestauranteId(idRestaurante);
        List<FuncionamentoDTO> expectedList = Arrays.asList(funcionamentoDTO);

        when(service.buscarPorRestaurante(idRestaurante)).thenReturn(expectedList);

        ResponseEntity<List<FuncionamentoDTO>> response = controller.buscarPorRestaurante(idRestaurante);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
        verify(service, times(1)).buscarPorRestaurante(idRestaurante);
    }
}
