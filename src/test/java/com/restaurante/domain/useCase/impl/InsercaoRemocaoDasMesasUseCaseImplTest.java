package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.MesaService;
import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.infra.exceptions.CapacidadeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class InsercaoRemocaoDasMesasUseCaseImplTest {

    @Mock
    private MesaService mesaService;

    @InjectMocks
    private InsercaoRemocaoDasMesasUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Teste para executar use case com capacidade original diferente da atualizada")
    void testExecuteCapacidadeDiferente() {
        Long idRestaurante = 1L;
        int capacidadeOriginal = 5;
        int capacidadeAtualizada = 10;

        List<MesaDTO> mesaDTOs = new ArrayList<>();
        for (int i = 1; i <= capacidadeOriginal; i++) {
            MesaDTO mesaDTO = new MesaDTO();
            mesaDTO.setId((long) i);
            mesaDTO.setNomeMesa("MESA " + i);
            mesaDTOs.add(mesaDTO);
        }

        when(mesaService.findAllByIdRestaurante(idRestaurante)).thenReturn(mesaDTOs);

        useCase.execute(idRestaurante, capacidadeOriginal, capacidadeAtualizada);

        verify(mesaService, times(0)).delete(anyLong());
        verify(mesaService, times(1)).salvarTodasMesas(anyList());
    }

    @Test
    @DisplayName("Teste para executar use case com capacidade atualizada igual à original")
    void testExecuteCapacidadeIgual() {
        Long idRestaurante = 1L;
        int capacidadeOriginal = 5;
        int capacidadeAtualizada = 5;

        useCase.execute(idRestaurante, capacidadeOriginal, capacidadeAtualizada);

        verify(mesaService, never()).delete(anyLong());
        verify(mesaService, never()).salvarTodasMesas(anyList());
    }

    @Test
    @DisplayName("Teste para exceção quando capacidade atualizada é menor ou igual a zero")
    void testExecuteCapacidadeInvalida() {
        Long idRestaurante = 1L;
        int capacidadeOriginal = 5;
        int capacidadeAtualizada = 0;

        assertThrows(CapacidadeException.class, () -> useCase.execute(idRestaurante, capacidadeOriginal, capacidadeAtualizada));

        verify(mesaService, never()).delete(anyLong());
        verify(mesaService, never()).salvarTodasMesas(anyList());
    }
}
