package com.restaurante.infra.repository.postgres;

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
import java.util.List;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestauranteRepositoryIT {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
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
    class BuscarRestauranteRepositoryIT {
        @Test
        void testBuscarRestaurantesPorFiltro() {
            RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));

            List<RestauranteEntity> resultado = restauranteRepository.buscarRestaurantes(
                    restauranteSaved.getNome(),
                    restauranteSaved.getLocalizacao(),
                    restauranteSaved.getTipoCozinha().name()
            );

            assertNotNull(resultado);
            assertThat(resultado.size()).isPositive();
        }

        @Test
        void testBuscarPorId() {
            RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
            var result = restauranteRepository.findById(restauranteSaved.getId());
            assertTrue(result.isPresent());
            assertThat(result.get().getId()).isPositive();
        }
    }

    @Nested
    class SalvarRestauranteRepositoryIT {
        @Test
        void testSalvarRestaurante() {
            RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
            assertNotNull(restauranteSaved);
            assertThat(restauranteSaved.getId()).isPositive();
        }

        @Test
        void testAtualizarRestaurante() {
            var entity = getRandom(RestauranteEntity.class);
            entity.setNome("Primeiro nome");
            RestauranteEntity restauranteSaved = restauranteRepository.save(entity);
            var restauranteAtualizar = restauranteRepository.findById(restauranteSaved.getId());
            restauranteAtualizar.get().setNome("Atualizar nome");
            var result = restauranteRepository.save(restauranteAtualizar.get());

            assertNotNull(result);
            assertThat(result.getId()).isEqualTo(restauranteSaved.getId());
            assertThat(result.getNome()).isNotEqualTo(entity.getNome());
        }
    }

    @Nested
    class DeletarRestauranteRepositoryIT {
        @Test
        void testDeletarRestaurante() {
            RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));

            restauranteRepository.deleteById(restauranteSaved.getId());

            var restauranteAtualizar = restauranteRepository.findById(restauranteSaved.getId());
            assertFalse(restauranteAtualizar.isPresent());
        }
    }
}
