package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.useCase.BuscarMesaDisponivelUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ValidarReservaUseCaseImplTest {

    AutoCloseable openMocks;

    @InjectMocks
    private ValidarReservaUseCaseImpl validarReservaUseCase;

    @Mock
    private BuscarMesaDisponivelUseCase buscarMesaDisponivelUseCase;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class ValidarReservaUseCaseTest{
        @Test
        void testExecuteSuccess() {
            ReservaDTO reservaDTO = new ReservaDTO();
            reservaDTO.setRestauranteId(1L);
            reservaDTO.setDataDaReserva(LocalDateTime.now());

            MesaDisponivelDTO mesaDisponivelDTO = new MesaDisponivelDTO();
            mesaDisponivelDTO.setMesaId(10L);

            when(buscarMesaDisponivelUseCase.execute(anyLong(), any())).thenReturn(mesaDisponivelDTO);

            validarReservaUseCase.execute(reservaDTO);

            assertEquals(10L, reservaDTO.getMesaId());
            verify(buscarMesaDisponivelUseCase, times(1)).execute(anyLong(), any());
        }
    }
}
