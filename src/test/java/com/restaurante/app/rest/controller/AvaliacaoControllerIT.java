package com.restaurante.app.rest.controller;

import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
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
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvaliacaoControllerIT extends BaseUnitTest {

    @LocalServerPort
    private int port;

    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    RestauranteRepository restauranteRepository;

    @BeforeEach
    public void setup() {
        RestAssured.port = port > 0 ? port : 8080;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void testAvaliar() {
        UsuarioEntity usuarioEntity = usuarioRepository.save(getRandom(UsuarioEntity.class));
        RestauranteEntity restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));

        AvaliacaoDTO request = getRandom(AvaliacaoDTO.class);
        request.setNota(5);
        request.setUsuarioId(usuarioEntity.getId());
        request.setRestauranteId(restauranteEntity.getId());
        request.setComentario("Ótima comida!");

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/avaliacao")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasKey("id"))
                .body("$", hasKey("restauranteId"))
                .body("$", hasKey("usuarioId"))
                .body("$", hasKey("nota"))
                .body("$", hasKey("comentario"));
    }


    @Test
    void testAvaliarExcecaoQuandoJsonInvalido() {
        AvaliacaoDTO request = getRandom(AvaliacaoDTO.class);
        request.setNota(5);
        request.setUsuarioId(null);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/avaliacao")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("erro", equalTo("Erro na validação de dados"))
                .body("detalhe", equalTo("O usuário não pode ser nulo. Por favor, forneça um valor para o usuário."))
                .body("campo", equalTo("restauranteId"))
                .body("statusCode", equalTo(400));
    }

}