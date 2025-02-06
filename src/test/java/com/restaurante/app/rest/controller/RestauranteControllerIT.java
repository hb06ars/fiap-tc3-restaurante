package com.restaurante.app.rest.controller;

import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.TipoCozinhaEnum;
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
import static org.hamcrest.Matchers.hasKey;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RestauranteControllerIT extends BaseUnitTest {

    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;
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
    class CadastrarRestauranteControllerIT {
        @Test
        void cadastrarDeveRetornarRestauranteDTO() {
            var request = getRestauranteEntity();

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/restaurante")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("id"))
                    .body("$", hasKey("nome"))
                    .body("$", hasKey("localizacao"))
                    .body("$", hasKey("tipoCozinha"))
                    .body("$", hasKey("capacidade"));
        }
    }


    @Nested
    class AtualizarRestauranteControllerIT{
        @Test
        void atualizarDeveRetornarRestauranteDTOAtualizado() {
            var request = getRestauranteEntity();
            var restauranteSaved = restauranteRepository.save(request);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(restauranteSaved)
                    .when()
                    .put("/restaurante/1")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("id"))
                    .body("$", hasKey("nome"))
                    .body("$", hasKey("localizacao"))
                    .body("$", hasKey("tipoCozinha"))
                    .body("$", hasKey("capacidade"));
        }

        @Test
        void testAtualizarExcecaoQuandoIdNaoEncontrado() {
            var request = getRestauranteEntity();
            request.setNome("Teste 1");
            var restauranteSaved = restauranteRepository.save(request);
            restauranteSaved.setNome("Teste 2");

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(restauranteSaved)
                    .when()
                    .put("/restaurante/9")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("message[0].erro", equalTo("O objeto solicitado não foi encontrado no sistema"))
                    .body("message[0].detalhe", equalTo("Restaurante não encontrado no sistema!"));
        }
    }


    @Nested
    class BuscarRestauranteControllerIT{
        @Test
        void buscarDeveRetornarListaDeRestaurantes() {
            var request = getRestauranteEntity();
            restauranteRepository.save(request);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/restaurante?nome=Fogo de Chão&localizacao=Av. Pacaembú, 123&tipoCozinha=BRASILEIRA")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("[0]", hasKey("id"))
                    .body("[0]", hasKey("nome"))
                    .body("[0]", hasKey("localizacao"))
                    .body("[0]", hasKey("tipoCozinha"))
                    .body("[0]", hasKey("capacidade"));
        }
    }



    @Nested
    class RemoverRestauranteControllerIT{
        @Test
        void deletarDeveRetornarMensagemDeSucesso() {
            var request = getRestauranteEntity();
            request.setCapacidade(1);
            restauranteRepository.save(request);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .delete("/restaurante/1")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("mensagem"));
        }
    }


    private RestauranteEntity getRestauranteEntity() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setNome("Fogo de Chão");
        restauranteEntity.setLocalizacao("Av. Pacaembú, 123");
        restauranteEntity.setTipoCozinha(TipoCozinhaEnum.BRASILEIRA);
        restauranteEntity.setCapacidade(5);
        return restauranteEntity;
    }

}