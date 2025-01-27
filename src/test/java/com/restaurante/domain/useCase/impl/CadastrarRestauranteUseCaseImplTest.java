package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.useCase.InsercaoRemocaoDasMesasUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CadastrarRestauranteUseCaseImplTest {

    @Mock
    private RestauranteService restauranteService;

    @Mock
    private FuncionamentoService funcionamentoService;

    @Mock
    private InsercaoRemocaoDasMesasUseCase insercaoRemocaoDasMesasUseCase;

    @InjectMocks
    private CadastrarRestauranteUseCaseImpl cadastrarRestauranteUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void execute_DeveCadastrarRestauranteQuandoNaoExistir() {
        RestauranteDTO restauranteDTO = new RestauranteDTO();
        restauranteDTO.setNome("Restaurante Teste");
        restauranteDTO.setLocalizacao("Rua A");
        restauranteDTO.setCapacidade(50);

        when(restauranteService.restauranteJaExiste(restauranteDTO.getNome(), restauranteDTO.getLocalizacao())).thenReturn(false);
        when(restauranteService.save(restauranteDTO)).thenReturn(restauranteDTO);

        RestauranteDTO resultado = cadastrarRestauranteUseCase.execute(restauranteDTO);

        assertNotNull(resultado);
        verify(restauranteService).restauranteJaExiste(restauranteDTO.getNome(), restauranteDTO.getLocalizacao());
        verify(restauranteService).save(restauranteDTO);
        verify(insercaoRemocaoDasMesasUseCase).execute(resultado.getId(), 0, resultado.getCapacidade());
        verify(funcionamentoService).inserirDataFuncionamentoInicial(resultado.getId());
    }

    @Test
    void execute_DeveLancarExcecaoQuandoRestauranteJaExistir() {
        RestauranteDTO restauranteDTO = new RestauranteDTO();
        restauranteDTO.setNome("Restaurante Teste");
        restauranteDTO.setLocalizacao("Rua A");

        when(restauranteService.restauranteJaExiste(restauranteDTO.getNome(), restauranteDTO.getLocalizacao())).thenReturn(true);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cadastrarRestauranteUseCase.execute(restauranteDTO));

        assertEquals("O Restaurante já existe, tente atualizar o mesmo!", exception.getMessage());
        verify(restauranteService).restauranteJaExiste(restauranteDTO.getNome(), restauranteDTO.getLocalizacao());
        verify(restauranteService, never()).save(any());
        verify(insercaoRemocaoDasMesasUseCase, never()).execute(anyLong(), anyInt(), anyInt());
        verify(funcionamentoService, never()).inserirDataFuncionamentoInicial(anyLong());
    }
}
