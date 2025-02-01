package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.useCase.InserirRemoverMesasUseCase;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AtualizarRestauranteUseCaseImplTest {

    AutoCloseable openMocks;

    @InjectMocks
    private AtualizarRestauranteUseCaseImpl atualizarRestauranteUseCase;

    @Mock
    private RestauranteService restauranteService;

    @Mock
    private InserirRemoverMesasUseCase insercaoRemocaoDasMesasUseCase;

    private RestauranteDTO restauranteDTO;
    private RestauranteDTO restauranteOriginal;
    private Long id;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        id = 1L;
        restauranteDTO = new RestauranteDTO();
        restauranteDTO.setId(id);
        restauranteDTO.setNome("Restaurante Teste");
        restauranteDTO.setCapacidade(100);

        restauranteOriginal = new RestauranteDTO();
        restauranteOriginal.setId(id);
        restauranteOriginal.setNome("Restaurante Original");
        restauranteOriginal.setCapacidade(50);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }


    @Test
    void testExecute_Success() {
        when(restauranteService.findById(id)).thenReturn(restauranteOriginal);
        when(restauranteService.update(id, restauranteDTO)).thenReturn(restauranteDTO);

        RestauranteDTO result = atualizarRestauranteUseCase.execute(id, restauranteDTO);

        assertNotNull(result);
        assertEquals(restauranteDTO.getNome(), result.getNome());
        assertEquals(restauranteDTO.getCapacidade(), result.getCapacidade());

        verify(insercaoRemocaoDasMesasUseCase, times(1))
                .execute(restauranteOriginal.getId(), restauranteOriginal.getCapacidade(), restauranteDTO.getCapacidade());
        verify(restauranteService, times(1)).update(id, restauranteDTO);
    }

    @Test
    void testExecute_RestauranteNotFound() {
        when(restauranteService.findById(id)).thenReturn(null);
        assertThrows(ObjectNotFoundException.class, () -> atualizarRestauranteUseCase.execute(id, restauranteDTO));
        verify(restauranteService, never()).update(anyLong(), any());
    }
}
