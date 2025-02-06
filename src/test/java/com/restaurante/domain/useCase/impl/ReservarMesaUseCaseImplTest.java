package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.useCase.ValidarDataUseCase;
import com.restaurante.domain.useCase.ValidarReservaUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservarMesaUseCaseImplTest {

    AutoCloseable openMocks;

    @Mock
    private ValidarReservaUseCase validarReservaUseCase;

    @Mock
    private ValidarDataUseCase validaDataUseCase;

    @Mock
    private ReservaService reservaService;

    private ReservarMesaUseCaseImpl reservarMesaUseCase;

    private final Integer toleranciaMesa = 2;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        reservarMesaUseCase = new ReservarMesaUseCaseImpl(toleranciaMesa, validarReservaUseCase,
                validaDataUseCase, reservaService);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Nested
    class SalvarReservaMesaUseCaseTest {
        @Test
        void salvarDeveChamarValidacoesESalvarReserva() {
            ReservaDTO reservaDTO = new ReservaDTO();
            reservaDTO.setRestauranteId(1L);
            reservaDTO.setDataDaReserva(LocalDateTime.now());
            when(reservaService.save(any(ReservaDTO.class))).thenReturn(reservaDTO);

            ReservaDTO resultado = reservarMesaUseCase.salvar(reservaDTO);

            assertNotNull(resultado);
            verify(validarReservaUseCase).execute(reservaDTO);
            verify(validaDataUseCase).execute(any(), any(), any());
            verify(reservaService).save(reservaDTO);

            assertEquals(reservaDTO.getDataDaReserva().plusHours(toleranciaMesa), reservaDTO.getDataFimReserva());
        }
    }

    @Nested
    class AtualizarReservarMesaUseCaseTest {
        @Test
        void atualizarDeveAtualizarDadosDaReservaExistente() {
            Long id = 1L;
            ReservaDTO reservaOriginal = new ReservaDTO();
            reservaOriginal.setRestauranteId(1L);
            reservaOriginal.setDataDaReserva(LocalDateTime.now());
            reservaOriginal.setMesaId(1L);

            ReservaDTO reservaAtualizada = new ReservaDTO();
            reservaAtualizada.setRestauranteId(2L);
            reservaAtualizada.setDataDaReserva(LocalDateTime.now().plusDays(1));
            reservaAtualizada.setMesaId(2L);

            when(reservaService.findById(anyLong())).thenReturn(reservaOriginal);
            when(reservaService.save(any(ReservaDTO.class))).thenReturn(reservaAtualizada);

            ReservaDTO resultado = reservarMesaUseCase.atualizar(id, reservaAtualizada);

            assertNotNull(resultado);
            verify(reservaService).findById(id);
            verify(validarReservaUseCase).execute(reservaAtualizada);
            verify(validaDataUseCase).execute(any(), any(), any());
            verify(reservaService).save(reservaOriginal);

            assertNotEquals(reservaAtualizada.getMesaId(), reservaOriginal.getMesaId());
            assertNotEquals(reservaAtualizada.getRestauranteId(), reservaOriginal.getRestauranteId());
            assertEquals(reservaAtualizada.getDataDaReserva(), reservaOriginal.getDataDaReserva());
        }
    }

    @Nested
    class PreencherHorario {
        @Test
        void preencherHorarioDeSaidaDeveDefinirHorarioDeSaidaCorreto() {
            ReservaDTO reservaDTO = new ReservaDTO();
            reservaDTO.setDataDaReserva(LocalDateTime.now());
            reservarMesaUseCase.salvar(reservaDTO);
            assertEquals(reservaDTO.getDataDaReserva().plusHours(toleranciaMesa), reservaDTO.getDataFimReserva());
        }
    }
}
