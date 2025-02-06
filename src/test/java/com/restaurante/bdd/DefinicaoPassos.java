package com.restaurante.bdd;

import com.restaurante.app.rest.request.AvaliacaoRequest;
import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class DefinicaoPassos extends BaseUnitTest {

    private Response response;

    private AvaliacaoRequest request;

    private String ENDPOINT_MENSAGENS = "http://localhost:8080/avaliacao";

    @Quando("requisitar a lista da mensagem")
    public void requisitarListaMensagens() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT_MENSAGENS + "/1");
    }

    @Então("as avaliações sao exibidas com sucesso")
    public void as_avaliações_sao_exibidas_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/avaliacoes.json"));
    }


    @Quando("submeter uma nova mensagem")
    public AvaliacaoDTO submeter_uma_nova_mensagem() {
        request = getRandom(AvaliacaoRequest.class);
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(ENDPOINT_MENSAGENS);
        return response.then().extract().as(AvaliacaoDTO.class);
    }

    @Então("a mensagem é registrada com sucesso")
    public void a_mensagem_é_registrada_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.CREATED.value())
                .body(matchesJsonSchemaInClasspath("./schemas/avaliacoes.json"));
    }

}