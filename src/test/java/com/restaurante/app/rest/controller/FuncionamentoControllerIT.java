package com.restaurante.app.rest.controller;

import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.repository.postgres.FuncionamentoRepository;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.utils.BaseUnitTest;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FuncionamentoControllerIT extends BaseUnitTest {

    @LocalServerPort
    private int port;

    @Autowired
    RestauranteRepository restauranteRepository;
    @Autowired
    FuncionamentoRepository avaliacaoRepository;
    @Autowired
    FuncionamentoRepository funcionamentoRepository;

    @BeforeEach
    public void setup() {
        RestAssured.port = port > 0 ? port : 8080;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void testCadastrarFuncionamento() {
        RestauranteEntity restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
        FuncionamentoDTO request = getRandom(FuncionamentoDTO.class);
        request.setRestauranteId(restauranteEntity.getId());

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/funcionamento/cadastrar")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasKey("id"))
                .body("$", hasKey("diaEnum"))
                .body("$", hasKey("abertura"))
                .body("$", hasKey("fechamento"))
                .body("$", hasKey("restauranteId"));
    }

    @Test
    void testAvaliarExcecaoQuandoJsonInvalido() {
        FuncionamentoDTO request = getRandom(FuncionamentoDTO.class);
        request.setRestauranteId(null);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/funcionamento/cadastrar")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("erro", equalTo("Erro na validação de dados"))
                .body("detalhe", equalTo("O restaurante não pode ser nulo. Por favor, forneça um valor para o restaurante."))
                .body("campo", equalTo("restauranteId"))
                .body("statusCode", equalTo(400));
    }

    @Test
    void testAtualizarFuncionamento() {
        RestauranteEntity restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
        FuncionamentoEntity request = getRandom(FuncionamentoEntity.class);
        request.setDiaEnum(DiaEnum.SEGUNDA);
        request.setRestauranteId(restauranteEntity.getId());
        var saved = funcionamentoRepository.save(request);
        saved.setDiaEnum(DiaEnum.DOMINGO);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(saved)
                .when()
                .put("/funcionamento/atualizar/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasKey("id"))
                .body("$", hasKey("diaEnum"))
                .body("$", hasKey("abertura"))
                .body("$", hasKey("fechamento"))
                .body("$", hasKey("restauranteId"));
    }

    @Test
    void testAvaliarExcecaoQuandoIdNaoEncontrado() {
        FuncionamentoEntity request = getRandom(FuncionamentoEntity.class);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .put("/funcionamento/atualizar/9")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message[0].erro", equalTo("O objeto solicitado não foi encontrado no sistema"))
                .body("message[0].detalhe", equalTo("Funcionamento 9 não encontrado."))
                .body("statusCode", equalTo(404));
    }

    @Test
    void testDeletarFuncionamento() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        RestauranteEntity restauranteSaved = restauranteRepository.save(restauranteEntity);
        FuncionamentoEntity entity = getRandom(FuncionamentoEntity.class);
        entity.setRestauranteId(restauranteSaved.getId());
        avaliacaoRepository.save(entity);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete("/funcionamento/deletar/1")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void testBuscarPorRestaurante() {
        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/funcionamento/1")
                .then()
                .statusCode(HttpStatus.OK.value());
    }

}