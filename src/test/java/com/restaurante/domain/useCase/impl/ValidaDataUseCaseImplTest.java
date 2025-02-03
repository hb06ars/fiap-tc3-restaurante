package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.exceptions.ReservaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ValidaDataUseCaseImplTest {

    AutoCloseable openMocks;

    @InjectMocks
    private ValidarDataUseCaseImpl validaDataUseCase;

    @Mock
    private FuncionamentoService funcionamentoService;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testExecuteSuccess() {
        LocalDateTime dataReserva = LocalDateTime.of(2025, 1, 30, 18, 0);
        LocalDate diaSelecionado = LocalDate.of(2025, 1, 30);
        FuncionamentoDTO dto = FuncionamentoDTO.builder()
                .abertura(LocalTime.now())
                .fechamento(LocalTime.now().plusHours(2))
                .id(1L)
                .diaEnum(DiaEnum.DOMINGO).build();
        when(funcionamentoService.validarDataFuncionamento(anyLong(), any(), any())).thenReturn(List.of(dto));
        validaDataUseCase.execute(1L, dataReserva, diaSelecionado);
        verify(funcionamentoService, times(1)).validarDataFuncionamento(anyLong(), any(), any());
    }

    @Test
    void testExecute_DataInvalida() {
        LocalDateTime dataReserva = LocalDateTime.of(2025, 1, 30, 18, 0);
        LocalDate diaSelecionado = LocalDate.of(2025, 1, 30);
        when(funcionamentoService.validarDataFuncionamento(anyLong(), any(), any())).thenReturn(null);

        assertThrows(ReservaException.class, () -> validaDataUseCase.execute(1L, dataReserva, diaSelecionado));

        verify(funcionamentoService, times(1)).validarDataFuncionamento(anyLong(), any(), any());
    }

    @Test
    void testBuscaDiaDaSemana_Feriado() {
        LocalDate diaSelecionado = LocalDate.of(2025, 12, 25);
        DiaEnum diaEnum = validaDataUseCase.buscaDiaDaSemana(diaSelecionado);
        assertEquals(DiaEnum.FERIADOS, diaEnum);
    }

    @Test
    void testBuscaDiaDaSemana_Semana() {
        LocalDate diaSelecionado = LocalDate.of(2025, 1, 27);
        DiaEnum diaEnum = validaDataUseCase.buscaDiaDaSemana(diaSelecionado);
        assertEquals(DiaEnum.SEGUNDA, diaEnum);
    }
}
