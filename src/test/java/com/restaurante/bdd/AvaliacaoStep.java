package com.restaurante.bdd;

import com.restaurante.app.rest.request.AvaliacaoRequest;
import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@Transactional
public class AvaliacaoStep extends BaseUnitTest {

    private Response response;

    private AvaliacaoRequest request;

    private AvaliacaoDTO dto;

    private final String ENDPOINT = "http://localhost:8080/avaliacao";

    // Cadastrar
    @Quando("submeter uma nova mensagem")
    public AvaliacaoDTO submeterUmaNovaMensagem() {
        request = gerarNovaAvaliacao();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(ENDPOINT);
        return response.then().extract().as(AvaliacaoDTO.class);
    }

    @Então("a mensagem é registrada com sucesso")
    public void aMensagemRegistradaComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/avaliacao.json"));
    }

    // Buscar
    @Dado("que uma avaliacao ja exista")
    public void queUmaAvaliacaoJaExista() {
        dto = submeterUmaNovaMensagem();
    }

    @Quando("requisitar a lista da mensagem")
    public void requisitarListaMensagens() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto).when()
                .get(ENDPOINT + "/1");
    }

    @Então("as avaliações sao exibidas com sucesso")
    public void asAvaliacoesSaoExibidasComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/avaliacao-list.json"));
    }

    private AvaliacaoRequest gerarNovaAvaliacao() {
        AvaliacaoRequest requestRandom = new AvaliacaoRequest();
        requestRandom.setNota(5);
        requestRandom.setComentario("Ótimo");
        requestRandom.setDatapost(LocalDateTime.now());
        requestRandom.setUsuarioId(1L);
        requestRandom.setRestauranteId(1L);
        return requestRandom;
    }

}