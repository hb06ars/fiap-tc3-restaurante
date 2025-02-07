package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.utils.BaseUnitTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FuncionamentoServiceIT extends BaseUnitTest {

    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private FuncionamentoService funcionamentoService;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @BeforeEach
    public void setup() {
        RestAssured.port = port > 0 ? port : 8080;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    public void limparBancoDeDados() throws IOException {
        String sql = new String(Files.readAllBytes(Paths.get("src/test/resources/clean.sql")));
        jdbcTemplate.execute(sql);
    }

    @Nested
    class SalvarFuncionamentoServiceIT {
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
        void testInserirDataFuncionamentoInicial() {
            RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
            dto.setRestauranteId(restauranteSaved.getId());
            FuncionamentoDTO result = funcionamentoService.save(dto);
            funcionamentoService.inserirDataFuncionamentoInicial(result.getId());
        }
    }

    @Nested
    class BuscarFuncionamentoServiceIT {
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
        void testFindByIdFound() {
            FuncionamentoDTO foundFuncionamento = funcionamentoService.findById(1L);
            assertNull(foundFuncionamento);
        }

        @Test
        void testBuscarPorRestaurante() {
            RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
            dto.setRestauranteId(restauranteSaved.getId());
            FuncionamentoDTO result = funcionamentoService.save(dto);
            List<FuncionamentoDTO> funcionamentoList = funcionamentoService
                    .buscarPorRestaurante(result.getRestauranteId());
            assertNotNull(funcionamentoList);
        }
    }


    @Nested
    class AtualizarFuncionamentoServiceIT {
        @Test
        void testUpdateSuccess() {
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
        void testUpdateNotFound() {
            FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
            assertThrows(RuntimeException.class, () -> funcionamentoService.update(1L, dto));
        }
    }

    @Nested
    class DeletarFuncionamentoServiceIT {
        @Test
        void testDeleteSuccess() {
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
        void testDeleteNotFound() {
            assertThrows(RuntimeException.class, () -> funcionamentoService.delete(1L));
        }
    }

    @Nested
    class ValidarFuncionamentoServiceIT {
        @Test
        void testValidarDataFuncionamento() {
            RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            FuncionamentoDTO dto = getRandom(FuncionamentoDTO.class);
            dto.setAbertura(LocalTime.now().minusHours(2));
            dto.setDiaEnum(DiaEnum.SEGUNDA);
            dto.setFechamento(LocalTime.now().plusHours(1));
            dto.setRestauranteId(restauranteSaved.getId());
            FuncionamentoDTO result = funcionamentoService.save(dto);

            List<FuncionamentoDTO> funcionamentoList = funcionamentoService
                    .validarDataFuncionamento(result.getRestauranteId(), LocalDateTime.now(), DiaEnum.SEGUNDA);
            assertNotNull(funcionamentoList);
            assertThat(funcionamentoList.size()).isPositive();
        }
    }

}
