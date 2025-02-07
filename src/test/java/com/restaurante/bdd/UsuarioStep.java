package com.restaurante.bdd;

import com.restaurante.app.rest.request.UsuarioRequest;
import com.restaurante.domain.dto.UsuarioDTO;
import com.restaurante.utils.BaseUnitTest;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@ActiveProfiles("test")
public class UsuarioStep extends BaseUnitTest {

    private Response response;

    private UsuarioDTO dto;

    private final String ENDPOINT = "http://localhost:8080/usuario";

    // Cadastrar
    @Quando("submeter um novo Usuário")
    public UsuarioDTO submeterUmNovoUsuario() {
        UsuarioRequest request = gerarNovoUsuario();
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(ENDPOINT + "/cadastrar");
        return response.then().extract().as(UsuarioDTO.class);
    }

    @Então("o Usuário é salvo com sucesso")
    public void oUsuarioSalvoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/usuario.json"));
    }

    // Atualizar
    @Dado("que um Usuário já exista no sistema")
    public void queUmUsuarioJaExistaNoSistema() {
        dto = submeterUmNovoUsuario();
    }

    @Quando("requisitar a alteração do Usuário")
    public void requisitarAlteracaoDoUsuario() {
        dto.setNome("Teste");
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(dto)
                .when()
                .put(ENDPOINT + "/atualizar/{id}", dto.getId().toString());
    }

    @Então("o Usuário é atualizado com sucesso")
    public void oUsuarioAtualizadoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/usuario.json"));
    }

    // Buscar
    @Dado("que um Usuário já exista")
    public void queUmUsuarioJaExista() {
        dto = submeterUmNovoUsuario();
    }

    @Quando("requisitar a busca do Usuário")
    public void requisitarBuscaDouUsuario() {
        response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/usuario?celular={celular}", dto.getCelular());
    }

    @Então("o Usuário é exibido com sucesso")
    public void oUsuarioExibidoComSucesso() {
        response.then()
                .statusCode(HttpStatus.OK.value())
                .body(matchesJsonSchemaInClasspath("./schemas/usuario.json"));
    }

    private UsuarioRequest gerarNovoUsuario() {
        UsuarioRequest requestRandom = new UsuarioRequest();
        requestRandom.setEmail(RandomStringUtils.randomAlphabetic(12) + "@teste.com");
        requestRandom.setCelular(RandomStringUtils.randomNumeric(11));
        requestRandom.setNome(RandomStringUtils.randomAlphabetic(12));
        return requestRandom;
    }
}