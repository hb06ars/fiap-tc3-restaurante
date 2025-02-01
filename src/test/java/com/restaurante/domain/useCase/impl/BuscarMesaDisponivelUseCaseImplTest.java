package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.util.DataFormat;
import com.restaurante.infra.exceptions.ReservaException;
import com.restaurante.infra.repository.postgres.MesaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BuscarMesaDisponivelUseCaseImplTest {

    AutoCloseable openMocks;

    @Mock
    private MesaRepository mesaRepository;

    private BuscarMesaDisponivelUseCaseImpl buscarMesaDisponivelUseCase;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        buscarMesaDisponivelUseCase = new BuscarMesaDisponivelUseCaseImpl(mesaRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }


    @Test
    void execute_DeveRetornarMesaDisponivelDTO() {
        Long restauranteId = 1L;
        LocalDateTime dataReserva = LocalDateTime.now();

        List<Object[]> mesasDisponiveis = new ArrayList<>();
        mesasDisponiveis.add(new Object[]{1L, "Mesa 1", "Reservado"});

        when(mesaRepository.buscarMesasDisponiveis(restauranteId, DataFormat.truncate(dataReserva))).thenReturn(mesasDisponiveis);

        MesaDisponivelDTO resultado = buscarMesaDisponivelUseCase.execute(restauranteId, dataReserva);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getMesaId());
        assertEquals("Mesa 1", resultado.getMesaNome());
        assertEquals("Reservado", resultado.getStatusMesa());
        verify(mesaRepository).buscarMesasDisponiveis(restauranteId, DataFormat.truncate(dataReserva));
    }

    @Test
    void execute_DeveLancarReservaException_QuandoNaoHouverMesasDisponiveis() {
        Long restauranteId = 1L;
        LocalDateTime dataReserva = LocalDateTime.now();

        when(mesaRepository.buscarMesasDisponiveis(restauranteId, DataFormat.truncate(dataReserva))).thenReturn(new ArrayList<>());

        assertThrows(ReservaException.class, () -> buscarMesaDisponivelUseCase.execute(restauranteId, dataReserva));
        verify(mesaRepository).buscarMesasDisponiveis(restauranteId, DataFormat.truncate(dataReserva));
    }
}
