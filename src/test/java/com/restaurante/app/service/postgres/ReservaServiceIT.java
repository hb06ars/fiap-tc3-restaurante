package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.infra.exceptions.FieldNotFoundException;
import com.restaurante.infra.repository.postgres.ReservaRepository;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class ReservaServiceIT  extends BaseUnitTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ReservaService reservaService;

    private ReservaDTO reservaDTO;
    private ReservaEntity reservaEntity;

    @Test
    void save_ReturnsReservaDTO() {
        ReservaDTO result = reservaService.save(reservaDTO);
    }

    @Test
    void findAll_ReturnsReservaDTOList() {
        List<ReservaDTO> result = reservaService.findAll();
    }

    @Test
    void findById_ReturnsReservaDTO() {
        ReservaDTO result = reservaService.findById(1L);
    }

    @Test
    void findById_ReturnsNull_WhenReservaNotFound() {
        ReservaDTO result = reservaService.findById(1L);
    }

    @Test
    void update_ReturnsUpdatedReservaDTO() {
        ReservaDTO result = reservaService.update(1L, reservaDTO);
    }

    @Test
    void update_ThrowsRuntimeException_WhenReservaNotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> reservaService.update(1L, reservaDTO));
    }

    @Test
    void delete_Success() {
        reservaService.delete(1L);
    }

    @Test
    void delete_ThrowsRuntimeException_WhenReservaNotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> reservaService.delete(1L));
    }

    @Test
    void buscar_ThrowsFieldNotFoundException_WhenDataDaReservaIsNull() {
        FieldNotFoundException thrown = assertThrows(FieldNotFoundException.class, () -> reservaService.buscar(1L, reservaDTO));
    }

    @Test
    void buscar_ReturnsReservaDTOList() {
        List<ReservaDTO> result = reservaService.buscar(1L, reservaDTO);
    }

}
