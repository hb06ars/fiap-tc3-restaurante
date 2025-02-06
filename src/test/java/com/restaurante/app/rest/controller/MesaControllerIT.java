package com.restaurante.app.rest.controller;

import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.infra.repository.postgres.MesaRepository;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.utils.BaseUnitTest;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasKey;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MesaControllerIT extends BaseUnitTest {

    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    MesaRepository mesaRepository;
    @Autowired
    RestauranteRepository restauranteRepository;

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
    class CadastrarMesaControllerIT {
        @Test
        void testCadastrarMesa() {
            RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(2);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            var request = getRandom(MesaEntity.class);
            request.setRestauranteId(restauranteSaved.getId());

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/mesa/cadastrar")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("id"))
                    .body("$", hasKey("nomeMesa"))
                    .body("$", hasKey("restauranteId"));
        }

        @Test
        void testCadastrarExcecaoQuandoJsonInvalido() {
            RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(2);
            var request = getRandom(MesaEntity.class);
            request.setRestauranteId(null);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/mesa/cadastrar")
                    .then()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .body("erro", equalTo("Erro na validação de dados"))
                    .body("detalhe",
                            equalTo("O restaurante não pode ser nulo. " +
                                    "Por favor, forneça um valor para o restaurante."))
                    .body("campo", equalTo("restauranteId"))
                    .body("statusCode", equalTo(400));
        }
    }

    @Nested
    class AtualizarMesaControllerIT {
        @Test
        void testAtualizarMesa() {
            RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(2);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            var mesaSaved = getRandom(MesaEntity.class);
            mesaSaved.setRestauranteId(restauranteSaved.getId());
            mesaSaved.setNomeMesa("MESA 1");
            var request = mesaRepository.save(mesaSaved);
            request.setNomeMesa("MESA 2");

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .put("/mesa/atualizar/1")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("id"))
                    .body("$", hasKey("nomeMesa"))
                    .body("$", hasKey("restauranteId"));
        }

        @Test
        void testAtualizarExcecaoQuandoIdNaoEncontrado() {
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(1L);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(mesaEntity)
                    .when()
                    .put("/mesa/atualizar/9")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("message[0].erro", equalTo("Erro no sistema"))
                    .body("message[0].detalhe", equalTo("Mesa 9 não encontrada."));
        }
    }

    @Nested
    class BuscarMesaControllerIT {
        @Test
        void testBuscarMesaPorId() {
            RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(2);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            var request = getRandom(MesaEntity.class);
            request.setNomeMesa("MESA 1");
            request.setRestauranteId(restauranteSaved.getId());
            mesaRepository.save(request);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/mesa/1")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("mesaId", hasItem(1))
                    .body("mesaNome", hasItem("MESA 1"))
                    .body("statusMesa", hasItem("Disponível"));
        }

        @Test
        void testBuscarMesasPorRestaurante() {
            RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(2);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            var request = getRandom(MesaEntity.class);
            request.setNomeMesa("MESA 1");
            request.setRestauranteId(restauranteSaved.getId());
            mesaRepository.save(request);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/mesa/listaporrestaurante/1")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", hasItem(1))
                    .body("nomeMesa", hasItem("MESA 1"))
                    .body("restauranteId", hasItem(1));
        }
    }



}