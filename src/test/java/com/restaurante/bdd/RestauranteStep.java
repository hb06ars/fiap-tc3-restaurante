package com.restaurante.bdd;

import com.restaurante.app.rest.request.RestauranteRequest;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.enums.TipoCozinhaEnum;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;

public class RestauranteStep extends BaseUnitTest {

    private Response response;

    private RestauranteRequest request;

    private final String ENDPOINT = "http://localhost:8080/restaurante";

    private RestauranteDTO dto;

    // Salvar
    @Quando("submeter um novo Restaurante")
    public RestauranteDTO submeter_um_novo_restaurante() {
        request = gerarNovoRestaurante();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(ENDPOINT);
        return response.then().extract().as(RestauranteDTO.class);
    }

    @Então("o Restaurante é salvo com sucesso")
    public void o_restaurante_é_salvo_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/restaurante.json"));
    }

    // Atualizar
    @Dado("que um Restaurante já exista no sistema")
    public void que_um_restaurante_já_exista_no_sistema() {
        dto = submeter_um_novo_restaurante();
    }

    @Quando("requisitar a alteração do Restaurante")
    public void requisitar_a_alteração_do_restaurante() {
        dto.setNome("Restaurante Novo");
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .put(ENDPOINT + "/{id}", dto.getId().toString());
    }

    @Então("o Restaurante é atualizado com sucesso")
    public void o_restaurante_é_atualizado_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/restaurante.json"));
    }

    // Deletar
    @Dado("que um Restaurante já foi salvo")
    public void que_um_restaurante_já_foi_salvo() {
        dto = submeter_um_novo_restaurante();
    }

    @Quando("requisitar a exclusão do Restaurante")
    public void requisitar_a_exclusão_do_restaurante() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .delete(ENDPOINT + "/" + dto.getId());
    }

    @Então("o Restaurante é removido com sucesso")
    public void o_restaurante_é_removido_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("{\"mensagem\":\"Registro deletado com sucesso.\"}"));
    }

    // Buscar
    @Dado("que um Restaurante já exista")
    public void que_um_restaurante_já_exista() {
        dto = submeter_um_novo_restaurante();
    }

    @Quando("requisitar a busca do Restaurante")
    public void requisitar_a_busca_do_restaurante() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(ENDPOINT + "?nome=" + dto.getNome());
    }

    @Então("o Restaurante é exibido com sucesso")
    public void o_restaurante_é_exibido_com_sucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/restaurante-list.json"));
    }

    private RestauranteRequest gerarNovoRestaurante() {
        RestauranteRequest requestRandom = new RestauranteRequest();
        requestRandom.setNome(RandomStringUtils.randomAlphabetic(12));
        requestRandom.setLocalizacao(RandomStringUtils.randomAlphabetic(12));
        requestRandom.setTipoCozinha(TipoCozinhaEnum.BRASILEIRA);
        requestRandom.setCapacidade(3);
        return requestRandom;
    }

}