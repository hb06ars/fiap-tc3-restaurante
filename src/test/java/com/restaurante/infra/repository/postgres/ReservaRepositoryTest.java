package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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

class ReservaRepositoryTest {

    @Mock
    private ReservaRepository reservaRepository;

    private ReservaEntity reserva;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        reserva = new ReservaEntity();
        reserva.setId(1L);
        reserva.setRestauranteId(1L);
        reserva.setStatusReserva(StatusReservaEnum.OCUPADO);
        reserva.setStatusPagamento(StatusPagamentoEnum.PAGO);
        reserva.setDataDaReserva(LocalDateTime.of(2025, 2, 1, 19, 0));
        reserva.setDataFimReserva(LocalDateTime.of(2025, 2, 1, 21, 0));
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testBuscarReservasPorFiltro() {
        LocalDateTime dataReserva = LocalDateTime.of(2025, 2, 1, 19, 0);

        ReservaEntity reserva2 = new ReservaEntity();
        reserva2.setId(2L);
        reserva2.setRestauranteId(1L);
        reserva2.setStatusReserva(StatusReservaEnum.RESERVADO);
        reserva2.setStatusPagamento(StatusPagamentoEnum.PENDENTE);
        reserva2.setDataDaReserva(LocalDateTime.of(2025, 2, 1, 20, 0));
        reserva2.setDataFimReserva(LocalDateTime.of(2025, 2, 1, 22, 0));

        List<ReservaEntity> reservas = Arrays.asList(reserva, reserva2);

        when(reservaRepository.findAllByFilter(1L, StatusReservaEnum.RESERVADO.name(), StatusPagamentoEnum.PAGO.name(), dataReserva))
                .thenReturn(reservas);

        List<ReservaEntity> resultado = reservaRepository.findAllByFilter(1L, StatusReservaEnum.RESERVADO.name(), StatusPagamentoEnum.PAGO.name(), dataReserva);

        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        assertEquals(StatusReservaEnum.OCUPADO, resultado.get(0).getStatusReserva());
        assertEquals(StatusReservaEnum.RESERVADO, resultado.get(1).getStatusReserva());

        verify(reservaRepository, times(1)).findAllByFilter(1L, StatusReservaEnum.RESERVADO.name(), StatusPagamentoEnum.PAGO.name(), dataReserva);
    }

    @Test
    void testSalvarReserva() {
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        ReservaEntity savedReserva = reservaRepository.save(reserva);

        assertNotNull(savedReserva);
        assertEquals(1L, savedReserva.getId());
        assertEquals(1L, savedReserva.getRestauranteId());
        assertEquals(StatusReservaEnum.OCUPADO, savedReserva.getStatusReserva());

        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    void testBuscarPorId() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Optional<ReservaEntity> foundReserva = reservaRepository.findById(1L);

        assertTrue(foundReserva.isPresent());
        assertEquals(1L, foundReserva.get().getId());

        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    void testAtualizarReserva() {
        ReservaEntity reservaAtualizada = new ReservaEntity();
        reservaAtualizada.setId(1L);
        reservaAtualizada.setRestauranteId(1L);
        reservaAtualizada.setStatusReserva(StatusReservaEnum.CANCELADO);
        reservaAtualizada.setStatusPagamento(StatusPagamentoEnum.CANCELADO);
        reservaAtualizada.setDataDaReserva(reserva.getDataDaReserva());
        reservaAtualizada.setDataFimReserva(reserva.getDataFimReserva());

        when(reservaRepository.save(reservaAtualizada)).thenReturn(reservaAtualizada);
        ReservaEntity updatedReserva = reservaRepository.save(reservaAtualizada);

        assertEquals(StatusReservaEnum.CANCELADO, updatedReserva.getStatusReserva());
        assertEquals(StatusPagamentoEnum.CANCELADO, updatedReserva.getStatusPagamento());
        verify(reservaRepository, times(1)).save(reservaAtualizada);
    }

    @Test
    void testDeletarReserva() {
        doNothing().when(reservaRepository).deleteById(1L);
        reservaRepository.deleteById(1L);
        verify(reservaRepository, times(1)).deleteById(1L);
    }
}
