package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
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
import java.util.Optional;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MesaRepositoryIT {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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
    class BuscarMesaRepositoryIT {
        @Test
        void testBuscarMesasDisponiveis() {
            var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restaurante.getId());
            mesaRepository.save(mesaEntity);
            var mesas = mesaRepository.buscarMesasDisponiveis(mesaEntity.getId(), LocalDateTime.now());
            assertNotNull(mesas);
        }

        @Test
        void testBuscarTodasPorRestauranteId() {
            var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restaurante.getId());

            var result = mesaRepository.save(mesaEntity);
            var mesa = mesaRepository.findAllByRestauranteId(result.getRestauranteId());

            assertThat(mesa.size()).isPositive();
            assertThat(mesa.get(0).getRestauranteId()).isEqualTo(restaurante.getId());
        }

        @Test
        void testBuscarPorId() {
            var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restaurante.getId());
            MesaEntity savedMesa = mesaRepository.save(mesaEntity);

            Optional<MesaEntity> foundMesa = mesaRepository.findById(savedMesa.getId());

            assertTrue(foundMesa.isPresent());
            assertThat(foundMesa.get().getId()).isPositive();
        }
    }

    @Nested
    class SalvarMesaRepositoryIT {
        @Test
        void testSalvarMesa() {
            var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restaurante.getId());
            MesaEntity savedMesa = mesaRepository.save(mesaEntity);

            assertNotNull(savedMesa);
            assertThat(savedMesa.getId()).isPositive();
        }


        @Test
        void testAtualizarMesa() {
            var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restaurante.getId());
            MesaEntity savedMesa = mesaRepository.save(mesaEntity);
            savedMesa.setId(savedMesa.getId());
            savedMesa.setNomeMesa("Mesa atualizada");
            savedMesa = mesaRepository.save(savedMesa);

            assertNotNull(savedMesa);
            assertThat(savedMesa.getNomeMesa()).isNotEqualTo(mesaEntity.getNomeMesa());
        }
    }

    @Nested
    class DeletarMEsaRepositoryIT {
        @Test
        void testDeletarMesa() {
            var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restaurante.getId());
            MesaEntity savedMesa = mesaRepository.save(mesaEntity);

            mesaRepository.deleteById(savedMesa.getId());
            var mesa = mesaRepository.findById(savedMesa.getId());

            assertTrue(mesa.isEmpty());
        }
    }
}
