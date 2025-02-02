package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class MesaServiceIT extends BaseUnitTest {

    @Autowired
    private MesaService mesaService;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void save_ValidMesa_ReturnsMesaDTO() {
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(10);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
        mesaDTO.setRestauranteId(restauranteSaved.getId());

        MesaDTO savedMesaDTO = mesaService.save(mesaDTO);

        assertNotNull(savedMesaDTO);
        assertThat(savedMesaDTO.getId()).isPositive();
    }

    @Test
    void save_CapacidadeExcedida_ThrowsCapacidadeException() {
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(0);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
        mesaDTO.setRestauranteId(restauranteSaved.getId());
        assertThrows(CapacidadeException.class, () -> mesaService.save(mesaDTO));
    }

    @Test
    void save_RestauranteNaoEncontrado_ThrowsObjectNotFoundException() {
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
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
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(10);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
        mesaDTO.setRestauranteId(restauranteSaved.getId());
        MesaDTO mesaEntity = mesaService.save(mesaDTO);

        MesaDTO result = mesaService.update(mesaEntity.getId(), mesaEntity);

        assertNotNull(result);
        assertThat(result.getId()).isPositive();
    }

    @Test
    void update_MesaNaoEncontrada_ThrowsRuntimeException() {
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
        assertThrows(RuntimeException.class, () -> mesaService.update(1L, mesaDTO));
    }

    @Test
    void delete_ExistingMesa_DeletesMesa() {
        mesaService.delete(1L);
    }

    @Test
    void buscarMesas_ReturnsMesaDisponivelDTOList() {
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(10);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
        mesaDTO.setRestauranteId(restauranteSaved.getId());
        MesaDTO mesaEntity = mesaService.save(mesaDTO);

        List<MesaDisponivelDTO> mesasDisponiveis = mesaService.buscarMesas(mesaEntity.getId());
    }

    @Test
    void salvarTodasMesas_SavesAllMesas() {
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(10);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
        mesaDTO.setRestauranteId(restauranteSaved.getId());
        List<MesaDTO> result = mesaService.salvarTodasMesas(List.of(new MesaEntity(mesaDTO)));
        assertNotNull(result);
        assertThat(result.get(0).getId()).isPositive();
    }

    @Test
    void findAllByIdRestaurante_ReturnsMesaDTOList() {
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(10);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
        mesaDTO.setRestauranteId(restauranteSaved.getId());
        List<MesaDTO> result = mesaService.salvarTodasMesas(List.of(new MesaEntity(mesaDTO)));

        List<MesaDTO> mesas = mesaService.findAllByIdRestaurante(result.get(0).getRestauranteId());
        assertNotNull(mesas);
        assertThat(mesas.get(0).getId()).isPositive();
    }

    @Test
    void findAllByRestaurante_ReturnsListOfMesas_WhenRestauranteHasMesas() {
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(10);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
        mesaDTO.setRestauranteId(restauranteSaved.getId());
        List<MesaDTO> result = mesaService.salvarTodasMesas(List.of(new MesaEntity(mesaDTO)));

        List<MesaDTO> mesasDTO = mesaService.findAllByRestaurante(result.get(0).getRestauranteId());

        assertNotNull(mesasDTO);
        assertThat(mesasDTO.get(0).getId()).isPositive();
    }

    @Test
    void findAllByRestaurante_ReturnsEmptyList_WhenRestauranteHasNoMesas() {
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(10);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaDTO mesaDTO = getRandom(MesaDTO.class);
        mesaDTO.setRestauranteId(restauranteSaved.getId());
        List<MesaDTO> result = mesaService.salvarTodasMesas(List.of(new MesaEntity(mesaDTO)));
        List<MesaDTO> mesasDTO = mesaService.findAllByRestaurante(result.get(0).getRestauranteId());

        assertNotNull(mesasDTO);
        assertThat(mesasDTO.get(0).getId()).isPositive();
    }

}
