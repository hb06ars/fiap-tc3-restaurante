package com.restaurante.domain.useCase.impl;

import com.restaurante.app.service.postgres.MesaService;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.infra.exceptions.CapacidadeException;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InsercaoRemocaoDasMesasUseCaseImplIT {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MesaService mesaService;

    @Autowired
    private InserirRemoverMesasUseCaseImpl useCase;
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
    class ExecutarInsercaoRemocaoDasMesasUseCaseIT{
        @Test
        void testExecuteCapacidadeDiferente() {
            int capacidadeOriginal = 5;
            int capacidadeAtualizada = 10;
            RestauranteEntity entity = getRandom(RestauranteEntity.class);
            entity.setCapacidade(capacidadeOriginal);
            var resultado = restauranteRepository.save(entity);

            resultado.setCapacidade(capacidadeAtualizada);
            useCase.execute(resultado.getId(), capacidadeOriginal, capacidadeAtualizada);
        }

        @Test
        void testExecuteCapacidadeIgual() {
            int capacidadeOriginal = 10;
            int capacidadeAtualizada = 10;
            RestauranteEntity entity = getRandom(RestauranteEntity.class);
            entity.setCapacidade(capacidadeOriginal);
            var resultado = restauranteRepository.save(entity);

            resultado.setCapacidade(capacidadeAtualizada);
            useCase.execute(resultado.getId(), capacidadeOriginal, capacidadeAtualizada);
        }

        @Test
        void testExecuteCapacidadeInvalida() {
            int capacidadeOriginal = 10;
            int capacidadeAtualizada = 0;
            RestauranteEntity entity = getRandom(RestauranteEntity.class);
            entity.setCapacidade(capacidadeOriginal);
            var resultado = restauranteRepository.save(entity);

            resultado.setCapacidade(capacidadeAtualizada);
            assertThrows(CapacidadeException.class, () -> useCase.execute(resultado.getId(), capacidadeOriginal,
                    capacidadeAtualizada));
        }


        @Test
        void testExecuteCapacidadeMaiorQueOriginalDiferente() {
            int capacidadeOriginal = 5;
            int capacidadeAtualizada = 10;
            RestauranteEntity entity = getRandom(RestauranteEntity.class);
            entity.setCapacidade(capacidadeOriginal);
            var resultado = restauranteRepository.save(entity);

            resultado.setCapacidade(capacidadeAtualizada);
            useCase.execute(resultado.getId(), capacidadeOriginal, capacidadeAtualizada);
        }
    }
}
