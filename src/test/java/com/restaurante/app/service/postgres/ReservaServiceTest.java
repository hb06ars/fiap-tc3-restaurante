package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.infra.exceptions.FieldNotFoundException;
import com.restaurante.infra.repository.postgres.ReservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @InjectMocks
    private ReservaService reservaService;

    private ReservaDTO reservaDTO;
    private ReservaEntity reservaEntity;

    @BeforeEach
    void setUp() {
        reservaDTO = ReservaDTO.builder()
                .id(1L)
                .dataDaReserva(LocalDateTime.now())
                .dataFimReserva(LocalDateTime.now().plusHours(2))
                .mesaId(1L)
                .restauranteId(1L)
                .statusReserva(StatusReservaEnum.OCUPADO)
                .statusPagamento(StatusPagamentoEnum.PENDENTE)
                .valorReserva(BigDecimal.valueOf(100L))
                .build();
        reservaEntity = new ReservaEntity(reservaDTO);
    }

    @Test
    void save_ReturnsReservaDTO() {
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reservaEntity);
        ReservaDTO result = reservaService.save(reservaDTO);
        assertNotNull(result);
        assertEquals(reservaDTO.getId(), result.getId());
    }

    @Test
    void findAll_ReturnsReservaDTOList() {
        when(reservaRepository.findAll()).thenReturn(List.of(reservaEntity));
        List<ReservaDTO> result = reservaService.findAll();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void findById_ReturnsReservaDTO() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaEntity));
        ReservaDTO result = reservaService.findById(1L);
        assertNotNull(result);
        assertEquals(reservaDTO.getId(), result.getId());
    }

    @Test
    void findById_ReturnsNull_WhenReservaNotFound() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());
        ReservaDTO result = reservaService.findById(1L);
        assertNull(result);
    }

    @Test
    void update_ReturnsUpdatedReservaDTO() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaEntity));
        when(reservaRepository.save(any(ReservaEntity.class))).thenReturn(reservaEntity);
        ReservaDTO result = reservaService.update(1L, reservaDTO);
        assertNotNull(result);
        assertEquals(reservaDTO.getId(), result.getId());
    }

    @Test
    void update_ThrowsRuntimeException_WhenReservaNotFound() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> reservaService.update(1L, reservaDTO));
        assertEquals("Reserva 1 não encontrada.", thrown.getMessage());
    }

    @Test
    void delete_Success() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reservaEntity));
        reservaService.delete(1L);
        verify(reservaRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_ThrowsRuntimeException_WhenReservaNotFound() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.empty());
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> reservaService.delete(1L));
        assertEquals("Reserva com ID: 1, não encontrada.", thrown.getMessage());
    }

    @Test
    void buscar_ThrowsFieldNotFoundException_WhenDataDaReservaIsNull() {
        reservaDTO.setDataDaReserva(null);
        FieldNotFoundException thrown = assertThrows(FieldNotFoundException.class, () -> reservaService.buscar(1L, reservaDTO));
        assertEquals("Informe a data.", thrown.getMessage());
    }

    @Test
    void buscar_ReturnsReservaDTOList() {
        when(reservaRepository.findAllByFilter(anyLong(), anyString(), anyString(), any()))
                .thenReturn(List.of(reservaEntity));

        List<ReservaDTO> result = reservaService.buscar(1L, reservaDTO);
        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
