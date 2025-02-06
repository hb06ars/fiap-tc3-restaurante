package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestauranteServiceIT extends BaseUnitTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RestauranteService restauranteService;

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
    class CadastrarReservaServiceIT{
        @Test
        void saveReturnsRestauranteDTO() {
            RestauranteDTO restauranteDTO = getRandom(RestauranteDTO.class);
            restauranteDTO.setCapacidade(5);
            RestauranteDTO restauranteSaved = restauranteService.save(restauranteDTO);
            assertNotNull(restauranteSaved);
            assertThat(restauranteSaved.getId()).isPositive();
        }
    }

    @Nested
    class BuscarReservaServiceIT{
        @Test
        void findAllReturnsRestauranteDTOList() {
            RestauranteDTO restauranteDTO = getRandom(RestauranteDTO.class);
            restauranteDTO.setCapacidade(5);
            restauranteService.save(restauranteDTO);

            List<RestauranteDTO> result = restauranteService.findAll();

            assertNotNull(result);
            assertThat(result.size()).isPositive();
        }

        @Test
        void findByIdReturnsRestauranteDTO() {
            RestauranteDTO restauranteDTO = getRandom(RestauranteDTO.class);
            restauranteDTO.setCapacidade(5);
            RestauranteDTO restauranteSaved = restauranteService.save(restauranteDTO);

            RestauranteDTO result = restauranteService.findById(restauranteSaved.getId());

            assertNotNull(result);
            assertThat(result.getId()).isPositive();
        }

        @Test
        void findByIdReturnsNullWhenRestauranteNotFound() {
            RestauranteDTO result = restauranteService.findById(1L);
            assertNull(result);
        }

        @Test
        void restauranteJaExisteReturnsTrueWhenRestaurantExists() {
            RestauranteDTO restauranteDTO = getRandom(RestauranteDTO.class);
            restauranteDTO.setCapacidade(5);
            RestauranteDTO restauranteSaved = restauranteService.save(restauranteDTO);

            boolean resultado = restauranteService.restauranteJaExiste(restauranteSaved.getNome(),
                    restauranteSaved.getLocalizacao());

            assertTrue(resultado);
        }

        @Test
        void restauranteJaExisteReturnsFalseWhenRestaurantDoesNotExist() {
            boolean resultado = restauranteService
                    .restauranteJaExiste("Restaurante Teste", "Localização Teste");

            assertFalse(resultado);
        }

        @Test
        void buscarRestaurantesReturnsRestauranteDTOList() {
            RestauranteDTO restauranteDTO = getRandom(RestauranteDTO.class);
            restauranteDTO.setCapacidade(5);
            RestauranteDTO restauranteSaved = restauranteService.save(restauranteDTO);

            var result = restauranteService.buscarRestaurantes(restauranteSaved.getNome(),
                    restauranteSaved.getLocalizacao(), restauranteSaved.getTipoCozinha().name());

            assertNotNull(result);
            assertThat(result.size()).isPositive();
        }
    }

    @Nested
    class ListarReservaServiceIT{
        @Test
        void updateReturnsUpdatedRestauranteDTO() {
            RestauranteDTO restauranteDTO = getRandom(RestauranteDTO.class);
            restauranteDTO.setCapacidade(5);
            RestauranteDTO restauranteSaved = restauranteService.save(restauranteDTO);

            restauranteSaved.setCapacidade(8);
            RestauranteDTO result = restauranteService.update(restauranteSaved.getId(), restauranteSaved);

            assertNotNull(result);
            assertThat(result.getId()).isPositive();
            assertThat(result.getCapacidade()).isEqualTo(8);
        }

        @Test
        void updateThrowsObjectNotFoundExceptionWhenRestauranteNotFound() {
            RestauranteDTO restauranteDTO = getRandom(RestauranteDTO.class);
            restauranteDTO.setCapacidade(5);
            assertThrows(ObjectNotFoundException.class, () -> restauranteService.update(1L, restauranteDTO));
        }
    }

    @Nested
    class DeletarReservaServiceIT{
        @Test
        void deleteSuccess() {
            RestauranteDTO restauranteDTO = getRandom(RestauranteDTO.class);
            restauranteDTO.setCapacidade(5);
            RestauranteDTO restauranteSaved = restauranteService.save(restauranteDTO);

            restauranteService.delete(restauranteSaved.getId());
            var result = restauranteService.findById(restauranteSaved.getId());

            assertNull(result);
        }

        @Test
        void deleteThrowsRuntimeExceptionWhenRestauranteNotFound() {
            assertThrows(RuntimeException.class, () -> restauranteService.delete(1L));
        }
    }
}
