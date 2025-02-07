package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.useCase.InserirRemoverMesasUseCase;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AtualizarRestauranteUseCaseImplIT extends BaseUnitTest {

    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AtualizarRestauranteUseCaseImpl atualizarRestauranteUseCase;

    @Autowired
    private RestauranteService restauranteService;

    @Autowired
    private InserirRemoverMesasUseCase insercaoRemocaoDasMesasUseCase;

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
    class ExecutarAtualizarRestauranteUseCaseIT {
        @Test
        void testExecuteSuccess() {
            RestauranteDTO dto = getRandom(RestauranteDTO.class);
            dto.setCapacidade(3);
            var restauranteSaved = restauranteService.save(dto);
            RestauranteDTO result = atualizarRestauranteUseCase.execute(restauranteSaved.getId(), restauranteSaved);
            assertNotNull(result);
        }

        @Test
        void testExecuteRestauranteNotFound() {
            RestauranteDTO dto = getRandom(RestauranteDTO.class);
            dto.setCapacidade(3);
            assertThrows(ObjectNotFoundException.class, () -> atualizarRestauranteUseCase.execute(1L, dto));
        }
    }
}
