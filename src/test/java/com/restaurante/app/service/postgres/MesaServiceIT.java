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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MesaServiceIT extends BaseUnitTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private MesaRepository mesaRepository;

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
    class SalvarMesaServiceIT {
        @Test
        void saveValidMesaReturnsMesaDTO() {
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
        void saveCapacidadeExcedidaThrowsCapacidadeException() {
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(0);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaDTO mesaDTO = getRandom(MesaDTO.class);
            mesaDTO.setRestauranteId(restauranteSaved.getId());
            assertThrows(CapacidadeException.class, () -> mesaService.save(mesaDTO));
        }

        @Test
        void saveRestauranteNaoEncontradoThrowsObjectNotFoundException() {
            MesaDTO mesaDTO = getRandom(MesaDTO.class);
            assertThrows(ObjectNotFoundException.class, () -> mesaService.save(mesaDTO));
        }

        @Test
        void salvarTodasMesasSavesAllMesas() {
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(10);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaDTO mesaDTO = getRandom(MesaDTO.class);
            mesaDTO.setRestauranteId(restauranteSaved.getId());
            List<MesaDTO> result = mesaService.salvarTodasMesas(List.of(new MesaEntity(mesaDTO)));
            assertNotNull(result);
            assertThat(result.get(0).getId()).isPositive();
        }
    }

    @Nested
    class ListarMesasServiceIT {
        @Test
        void findAllReturnsMesaDTOList() {
            List<MesaDTO> mesas = mesaService.findAll();
            assertNotNull(mesas);
        }

        @Test
        void findByIdExistingMesaReturnsMesaDTO() {
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(10);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaDTO mesaDTO = getRandom(MesaDTO.class);
            mesaDTO.setRestauranteId(restauranteSaved.getId());

            MesaDTO savedMesaDTO = mesaService.save(mesaDTO);

            MesaDTO foundMesa = mesaService.findById(savedMesaDTO.getId());
            assertNotNull(foundMesa);
        }

        @Test
        void findByIdMesaNaoEncontradaReturnsNull() {
            MesaDTO notFoundMesa = mesaService.findById(1L);
            assertNull(notFoundMesa);
        }

        @Test
        void buscarMesasReturnsMesaDisponivelDTOList() {
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(10);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaDTO mesaDTO = getRandom(MesaDTO.class);
            mesaDTO.setRestauranteId(restauranteSaved.getId());
            MesaDTO mesaEntity = mesaService.save(mesaDTO);

            List<MesaDisponivelDTO> mesasDisponiveis = mesaService.buscarMesas(mesaEntity.getId());
            assertNotNull(mesasDisponiveis);
        }

        @Test
        void findAllByIdRestauranteReturnsMesaDTOList() {
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
        void findAllByRestauranteReturnsListOfMesasWhenRestauranteHasMesas() {
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

    @Nested
    class AtualizarMesaServiceIT {
        @Test
        void updateExistingMesaReturnsUpdatedMesaDTO() {
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
        void updateMesaNaoEncontradaThrowsRuntimeException() {
            MesaDTO mesaDTO = getRandom(MesaDTO.class);
            assertThrows(RuntimeException.class, () -> mesaService.update(1L, mesaDTO));
        }
    }

    @Nested
    class DeletarMesaServiceIT {
        @Test
        void deleteExistingMesaDeletesMesa() {
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(10);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaDTO mesaDTO = getRandom(MesaDTO.class);
            mesaDTO.setRestauranteId(restauranteSaved.getId());
            MesaDTO savedMesaDTO = mesaService.save(mesaDTO);

            mesaService.delete(savedMesaDTO.getId());
            var result = mesaService.findById(savedMesaDTO.getId());
            assertNull(result);
        }
    }

}
