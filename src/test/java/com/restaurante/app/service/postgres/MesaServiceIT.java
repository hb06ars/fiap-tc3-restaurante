package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.infra.exceptions.CapacidadeException;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import com.restaurante.infra.repository.postgres.MesaRepository;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
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
class MesaServiceIT  extends BaseUnitTest {

    @Autowired
    private MesaService mesaService;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private MesaDTO mesaDTO;
    private MesaEntity mesaEntity;

    @Test
    void save_ValidMesa_ReturnsMesaDTO() {
        MesaDTO savedMesaDTO = mesaService.save(mesaDTO);
    }

    @Test
    void save_CapacidadeExcedida_ThrowsCapacidadeException() {
        assertThrows(CapacidadeException.class, () -> mesaService.save(mesaDTO));
    }

    @Test
    void save_RestauranteNaoEncontrado_ThrowsObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> mesaService.save(mesaDTO));
    }

    @Test
    void findAll_ReturnsMesaDTOList() {
        List<MesaDTO> mesas = mesaService.findAll();
    }

    @Test
    void findById_ExistingMesa_ReturnsMesaDTO() {
        MesaDTO foundMesa = mesaService.findById(1L);
    }

    @Test
    void findById_MesaNaoEncontrada_ReturnsNull() {
        MesaDTO foundMesa = mesaService.findById(1L);
    }

    @Test
    void update_ExistingMesa_ReturnsUpdatedMesaDTO() {
        MesaDTO updatedMesaDTO = mesaService.update(1L, mesaDTO);
    }

    @Test
    void update_MesaNaoEncontrada_ThrowsRuntimeException() {
        assertThrows(RuntimeException.class, () -> mesaService.update(1L, mesaDTO));
    }

    @Test
    void delete_ExistingMesa_DeletesMesa() {
        mesaService.delete(1L);
    }

    @Test
    void buscarMesas_ReturnsMesaDisponivelDTOList() {
        List<MesaDisponivelDTO> mesasDisponiveis = mesaService.buscarMesas(1L);
    }

    @Test
    void salvarTodasMesas_SavesAllMesas() {
        List<MesaDTO> savedMesas = mesaService.salvarTodasMesas(null);
    }

    @Test
    void findAllByIdRestaurante_ReturnsMesaDTOList() {
        List<MesaDTO> mesas = mesaService.findAllByIdRestaurante(1L);
    }

    @Test
    void findAllByRestaurante_ReturnsListOfMesas_WhenRestauranteHasMesas() {
        List<MesaDTO> mesasDTO = mesaService.findAllByRestaurante(1L);
    }

    @Test
    void findAllByRestaurante_ReturnsEmptyList_WhenRestauranteHasNoMesas() {
        List<MesaDTO> mesasDTO = mesaService.findAllByRestaurante(1L);
    }

    @Test
    void save_MesasIsEmpty_ThrowsCapacidadeException() {
        assertThrows(CapacidadeException.class, () -> mesaService.save(mesaDTO));
    }
}
