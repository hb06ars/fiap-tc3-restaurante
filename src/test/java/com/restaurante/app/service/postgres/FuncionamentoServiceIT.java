package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FuncionamentoServiceIT extends BaseUnitTest {

    @Autowired
    private FuncionamentoService funcionamentoService;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void testSave() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
        dto.setRestauranteId(restauranteSaved.getId());
        FuncionamentoDTO result = funcionamentoService.save(dto);
        assertNotNull(result);
        assertThat(result.getId()).isPositive();
    }

    @Test
    void testFindAll() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
        dto.setRestauranteId(restauranteSaved.getId());
        funcionamentoService.save(dto);
        List<FuncionamentoDTO> funcionamentoList = funcionamentoService.findAll();
        assertNotNull(funcionamentoList);
        assertThat(funcionamentoList.size()).isPositive();
    }

    @Test
    void testFindById_Found() {
        FuncionamentoDTO foundFuncionamento = funcionamentoService.findById(1L);
        assertNull(foundFuncionamento);
    }

    @Test
    void testUpdate_Success() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
        dto.setDiaEnum(DiaEnum.DOMINGO);
        dto.setRestauranteId(restauranteSaved.getId());
        FuncionamentoDTO result = funcionamentoService.save(dto);
        result.setDiaEnum(DiaEnum.SABADO);
        FuncionamentoDTO updatedFuncionamento = funcionamentoService.update(result.getId(), result);

        assertNotNull(updatedFuncionamento);
        assertThat(updatedFuncionamento.getId()).isPositive();
    }

    @Test
    void testUpdate_NotFound() {
        FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
        assertThrows(RuntimeException.class, () -> funcionamentoService.update(1L, dto));
    }

    @Test
    void testDelete_Success() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
        dto.setRestauranteId(restauranteSaved.getId());
        FuncionamentoDTO result = funcionamentoService.save(dto);
        funcionamentoService.delete(result.getId());
        var item = funcionamentoService.findById(result.getId());
        assertNull(item);
    }

    @Test
    void testDelete_NotFound() {
        assertThrows(RuntimeException.class, () -> funcionamentoService.delete(1L));
    }

    @Test
    void testValidarDataFuncionamento() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
        dto.setAbertura(LocalTime.now());
        dto.setDiaEnum(DiaEnum.SEGUNDA);
        dto.setFechamento(LocalTime.now().plusHours(2));
        dto.setRestauranteId(restauranteSaved.getId());
        FuncionamentoDTO result = funcionamentoService.save(dto);

        List<FuncionamentoDTO> funcionamentoList = funcionamentoService.validarDataFuncionamento(result.getRestauranteId(), LocalDateTime.now(), DiaEnum.SEGUNDA);
        assertNotNull(funcionamentoList);
        assertThat(funcionamentoList.size()).isPositive();
    }

    @Test
    void testBuscarPorRestaurante() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
        dto.setRestauranteId(restauranteSaved.getId());
        FuncionamentoDTO result = funcionamentoService.save(dto);
        List<FuncionamentoDTO> funcionamentoList = funcionamentoService.buscarPorRestaurante(result.getRestauranteId());
        assertNotNull(funcionamentoList);
    }

    @Test
    void testInserirDataFuncionamentoInicial() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
        dto.setRestauranteId(restauranteSaved.getId());
        FuncionamentoDTO result = funcionamentoService.save(dto);
        funcionamentoService.inserirDataFuncionamentoInicial(result.getId());
    }
}
