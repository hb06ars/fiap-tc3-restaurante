package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.RestauranteDTO;
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

import static com.restaurante.utils.TestUtil.getRandom;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CadastrarRestauranteUseCaseImplIT {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CadastrarRestauranteUseCaseImpl cadastrarRestauranteUseCase;

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
    class ExecutarCadastrarRestauranteUseCaseIT {
        @Test
        void executeDeveRetornarMesaDisponivelDTO() {
            RestauranteDTO dto = getRandom(RestauranteDTO.class);
            dto.setCapacidade(3);
            RestauranteDTO resultado = cadastrarRestauranteUseCase.execute(dto);
            assertNotNull(resultado);
        }

        @Test
        void executeDeveLancarReservaExceptionQuandoNaoHouverMesasDisponiveis() {
            RestauranteDTO dto = getRandom(RestauranteDTO.class);
            dto.setCapacidade(3);
            RestauranteDTO resultado = cadastrarRestauranteUseCase.execute(dto);
            assertThrows(RuntimeException.class, () -> cadastrarRestauranteUseCase.execute(resultado));
        }
    }
}
