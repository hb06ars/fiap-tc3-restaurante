package com.restaurante.infra.repository.postgres;


import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.enums.DiaEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FuncionamentoRepositoryTest {

    @Mock
    private FuncionamentoRepository funcionamentoRepository;

    private FuncionamentoEntity funcionamento;

    @BeforeEach
    void setUp() {
        funcionamento = new FuncionamentoEntity();
        funcionamento.setId(1L);
        funcionamento.setRestauranteId(1L);
        funcionamento.setDiaEnum(DiaEnum.SEGUNDA);
        funcionamento.setAbertura(LocalTime.of(8, 0));
        funcionamento.setFechamento(LocalTime.of(22, 0));
    }

    @Test
    void testValidarData() {
        LocalDateTime dataReserva = LocalDateTime.of(2025, 2, 1, 12, 0);
        List<FuncionamentoEntity> funcionamentoList = List.of(funcionamento);

        when(funcionamentoRepository.validarData(1L, dataReserva, DiaEnum.SEGUNDA.name())).thenReturn(funcionamentoList);

        List<FuncionamentoEntity> resultado = funcionamentoRepository.validarData(1L, dataReserva, "SEGUNDA");

        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals(1L, resultado.get(0).getRestauranteId());

        verify(funcionamentoRepository, times(1)).validarData(1L, dataReserva, "SEGUNDA");
    }

    @Test
    void testBuscarTodosFUncionamentosPorRestauranteId() {
        FuncionamentoDTO funcionamentoDTO1 = FuncionamentoDTO.builder()
                .id(1L)
                .diaEnum(DiaEnum.SEGUNDA)
                .abertura(LocalTime.of(8, 0))
                .fechamento(LocalTime.of(22, 0))
                .restauranteId(2L)
                .build();

        FuncionamentoDTO funcionamentoDTO2 = FuncionamentoDTO.builder()
                .id(1L)
                .diaEnum(DiaEnum.TERCA)
                .abertura(LocalTime.of(8, 0))
                .fechamento(LocalTime.of(22, 0))
                .restauranteId(2L)
                .build();

        List<FuncionamentoDTO> funcionamentoDTOList = Arrays.asList(funcionamentoDTO1, funcionamentoDTO2);

        when(funcionamentoRepository.findAllByRestauranteId(1L)).thenReturn(funcionamentoDTOList);

        List<FuncionamentoDTO> resultado = funcionamentoRepository.findAllByRestauranteId(1L);

        assertEquals(2, resultado.size());
        assertEquals(DiaEnum.SEGUNDA, resultado.get(0).getDiaEnum());
        assertEquals(DiaEnum.TERCA, resultado.get(1).getDiaEnum());

        verify(funcionamentoRepository, times(1)).findAllByRestauranteId(1L);
    }


    @Test
    void testSalvarFuncionamento() {
        when(funcionamentoRepository.save(funcionamento)).thenReturn(funcionamento);

        FuncionamentoEntity savedFuncionamento = funcionamentoRepository.save(funcionamento);

        assertNotNull(savedFuncionamento);
        assertEquals(1L, savedFuncionamento.getId());
        assertEquals(1L, savedFuncionamento.getRestauranteId());
        assertEquals(DiaEnum.SEGUNDA, savedFuncionamento.getDiaEnum());

        verify(funcionamentoRepository, times(1)).save(funcionamento);
    }

    @Test
    void testBuscarPorId() {
        when(funcionamentoRepository.findById(1L)).thenReturn(Optional.of(funcionamento));

        Optional<FuncionamentoEntity> foundFuncionamento = funcionamentoRepository.findById(1L);

        assertTrue(foundFuncionamento.isPresent());
        assertEquals(1L, foundFuncionamento.get().getId());

        verify(funcionamentoRepository, times(1)).findById(1L);
    }


    @Test
    void testAtualizarFuncionamento() {
        FuncionamentoEntity funcionamentoAtualizado = new FuncionamentoEntity();
        funcionamentoAtualizado.setId(1L);
        funcionamentoAtualizado.setRestauranteId(1L);
        funcionamentoAtualizado.setDiaEnum(DiaEnum.SEGUNDA);
        funcionamentoAtualizado.setAbertura(LocalTime.of(9, 0));
        funcionamentoAtualizado.setFechamento(LocalTime.of(21, 0));

        when(funcionamentoRepository.save(funcionamentoAtualizado)).thenReturn(funcionamentoAtualizado);
        FuncionamentoEntity updatedFuncionamento = funcionamentoRepository.save(funcionamentoAtualizado);

        assertEquals(LocalTime.of(9, 0), updatedFuncionamento.getAbertura());
        assertEquals(LocalTime.of(21, 0), updatedFuncionamento.getFechamento());

        verify(funcionamentoRepository, times(1)).save(funcionamentoAtualizado);
    }

    @Test
    void testDeletarFuncionamento() {
        doNothing().when(funcionamentoRepository).deleteById(1L);
        funcionamentoRepository.deleteById(1L);
        verify(funcionamentoRepository, times(1)).deleteById(1L);
    }
}
