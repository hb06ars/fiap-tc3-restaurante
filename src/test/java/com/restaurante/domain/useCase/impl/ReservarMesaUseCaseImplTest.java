package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.useCase.ValidaDataUseCase;
import com.restaurante.domain.useCase.ValidarReservaUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReservarMesaUseCaseImplTest {

    @Mock
    private ValidarReservaUseCase validarReservaUseCase;

    @Mock
    private ValidaDataUseCase validaDataUseCase;

    @Mock
    private ReservaService reservaService;

    private ReservarMesaUseCaseImpl reservarMesaUseCase;

    private final Integer toleranciaMesa = 2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reservarMesaUseCase = new ReservarMesaUseCaseImpl(toleranciaMesa, validarReservaUseCase, validaDataUseCase, reservaService);
    }

    @Test
    void salvar_DeveChamarValidacoesESalvarReserva() {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setRestauranteId(1L);
        reservaDTO.setDataDaReserva(LocalDateTime.now());
        when(reservaService.save(any(ReservaDTO.class))).thenReturn(reservaDTO);

        ReservaDTO resultado = reservarMesaUseCase.salvar(reservaDTO);

        assertNotNull(resultado);
        verify(validarReservaUseCase).execute(reservaDTO);
        verify(validaDataUseCase).execute(eq(reservaDTO.getRestauranteId()), eq(reservaDTO.getDataDaReserva()), eq(reservaDTO.getDataDaReserva().toLocalDate()));
        verify(reservaService).save(reservaDTO);

        assertEquals(reservaDTO.getDataDaReserva().plusHours(toleranciaMesa), reservaDTO.getDataFimReserva());
    }

    @Test
    void atualizar_DeveAtualizarDadosDaReservaExistente() {
        Long id = 1L;
        ReservaDTO reservaOriginal = new ReservaDTO();
        reservaOriginal.setRestauranteId(1L);
        reservaOriginal.setDataDaReserva(LocalDateTime.now());
        reservaOriginal.setMesaId(1L);

        ReservaDTO reservaAtualizada = new ReservaDTO();
        reservaAtualizada.setRestauranteId(2L);
        reservaAtualizada.setDataDaReserva(LocalDateTime.now().plusDays(1));
        reservaAtualizada.setMesaId(2L);

        when(reservaService.findById(id)).thenReturn(reservaOriginal);
        when(reservaService.save(any(ReservaDTO.class))).thenReturn(reservaAtualizada);

        ReservaDTO resultado = reservarMesaUseCase.atualizar(id, reservaAtualizada);

        assertNotNull(resultado);
        verify(reservaService).findById(id);
        verify(validarReservaUseCase).execute(reservaAtualizada);
        verify(validaDataUseCase).execute(eq(reservaAtualizada.getRestauranteId()), eq(reservaAtualizada.getDataDaReserva()), eq(reservaAtualizada.getDataDaReserva().toLocalDate()));
        verify(reservaService).save(reservaOriginal);

        assertEquals(reservaAtualizada.getMesaId(), reservaOriginal.getMesaId());
        assertEquals(reservaAtualizada.getRestauranteId(), reservaOriginal.getRestauranteId());
        assertEquals(reservaAtualizada.getDataDaReserva(), reservaOriginal.getDataDaReserva());
    }

    @Test
    void preencherHorarioDeSaida_DeveDefinirHorarioDeSaidaCorreto() {
        ReservaDTO reservaDTO = new ReservaDTO();
        reservaDTO.setDataDaReserva(LocalDateTime.now());
        reservarMesaUseCase.salvar(reservaDTO);
        assertEquals(reservaDTO.getDataDaReserva().plusHours(toleranciaMesa), reservaDTO.getDataFimReserva());
    }
}
