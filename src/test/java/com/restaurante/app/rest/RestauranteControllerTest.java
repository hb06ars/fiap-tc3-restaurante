package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.MessageSuccessDTO;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.useCase.AtualizarRestauranteUseCase;
import com.restaurante.domain.useCase.CadastrarRestauranteUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestauranteControllerTest {

    @Mock
    private RestauranteService restauranteService;

    @Mock
    private CadastrarRestauranteUseCase cadastrarRestauranteUseCase;

    @Mock
    private AtualizarRestauranteUseCase atualizarRestauranteUseCase;

    private RestauranteController restauranteController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restauranteController = new RestauranteController(restauranteService, cadastrarRestauranteUseCase, atualizarRestauranteUseCase);
    }

    @Test
    void cadastrar_DeveRetornarRestauranteDTO() {
        RestauranteDTO restauranteDTO = new RestauranteDTO();
        restauranteDTO.setNome("Restaurante Teste");

        when(cadastrarRestauranteUseCase.execute(restauranteDTO)).thenReturn(restauranteDTO);

        ResponseEntity<RestauranteDTO> response = restauranteController.cadastrar(restauranteDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(restauranteDTO, response.getBody());
        verify(cadastrarRestauranteUseCase).execute(restauranteDTO);
    }

    @Test
    void atualizar_DeveRetornarRestauranteDTOAtualizado() {
        Long id = 1L;
        RestauranteDTO restauranteDTO = new RestauranteDTO();
        restauranteDTO.setNome("Restaurante Atualizado");

        when(atualizarRestauranteUseCase.execute(id, restauranteDTO)).thenReturn(restauranteDTO);

        ResponseEntity<RestauranteDTO> response = restauranteController.atualizar(id, restauranteDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(restauranteDTO, response.getBody());
        verify(atualizarRestauranteUseCase).execute(id, restauranteDTO);
    }

    @Test
    void buscar_DeveRetornarListaDeRestaurantes() {
        List<RestauranteDTO> restaurantes = new ArrayList<>();
        RestauranteDTO restaurante1 = new RestauranteDTO();
        restaurante1.setNome("Restaurante 1");
        RestauranteDTO restaurante2 = new RestauranteDTO();
        restaurante2.setNome("Restaurante 2");
        restaurantes.add(restaurante1);
        restaurantes.add(restaurante2);

        when(restauranteService.buscarRestaurantes("", "", "")).thenReturn(restaurantes);

        ResponseEntity<List<RestauranteDTO>> response = restauranteController.buscar("", "", "");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(restaurantes, response.getBody());
        verify(restauranteService).buscarRestaurantes("", "", "");
    }

    @Test
    void deletar_DeveRetornarMensagemDeSucesso() {
        Long id = 1L;

        ResponseEntity<MessageSuccessDTO> response = restauranteController.deletar(id);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Registro deletado com sucesso.", response.getBody().getMensagem());
        verify(restauranteService).delete(id);
    }
}
