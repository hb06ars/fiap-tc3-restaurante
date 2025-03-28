package com.restaurante.app.rest.controller;

import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
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
class UsuarioControllerIT extends BaseUnitTest {

    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    UsuarioRepository usuarioRepository;

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
    class CadastrarUsuarioControllerIT {
        @Test
        void cadastrarDeveRetornarusuarioDTO() {
            var request = getUsuarioEntity();

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(request)
                    .when()
                    .post("/usuario/cadastrar")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("id"))
                    .body("$", hasKey("nome"))
                    .body("$", hasKey("email"))
                    .body("$", hasKey("celular"));
        }
    }


    @Nested
    class AtualizarUsuarioControllerIT {
        @Test
        void atualizarDeveRetornarusuarioDTOAtualizado() {
            var request = getUsuarioEntity();
            var usuarioSaved = usuarioRepository.save(request);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(usuarioSaved)
                    .when()
                    .put("/usuario/atualizar/1")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("id"))
                    .body("$", hasKey("nome"))
                    .body("$", hasKey("email"))
                    .body("$", hasKey("celular"));
        }

        @Test
        void testAtualizarExcecaoQuandoIdNaoEncontrado() {
            var request = getUsuarioEntity();
            request.setNome("Teste 1");
            var usuarioSaved = usuarioRepository.save(request);
            usuarioSaved.setNome("Teste 2");

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(usuarioSaved)
                    .when()
                    .put("/usuario/atualizar/9")
                    .then()
                    .statusCode(HttpStatus.NOT_FOUND.value())
                    .body("message[0].erro", equalTo("Erro no sistema"))
                    .body("message[0].detalhe", equalTo("Usuário 9 não encontrado."));
        }
    }

    @Nested
    class ListarUsuariosControllerIT {
        @Test
        void buscarDeveRetornarListaDeusuarios() {
            var request = getUsuarioEntity();
            usuarioRepository.save(request);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuario?celular=11988887777")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("id"))
                    .body("$", hasKey("nome"))
                    .body("$", hasKey("email"))
                    .body("$", hasKey("celular"));
        }

        @Test
        void deveRetornarUsuario() {
            var request = getUsuarioEntity();
            request.setCelular("11988887777");
            request.setEmail("email@email.com");
            request.setNome("Usuário");
            usuarioRepository.save(request);

            given()
                    .filter(new AllureRestAssured())
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .get("/usuario?celular=11988887777")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("$", hasKey("id"))
                    .body("$", hasKey("nome"))
                    .body("$", hasKey("email"))
                    .body("$", hasKey("celular"));
        }
    }

    private UsuarioEntity getUsuarioEntity() {
        UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);
        usuarioEntity.setNome("José Carlos");
        usuarioEntity.setCelular("11988887777");
        usuarioEntity.setEmail("teste@teste.com");
        return usuarioEntity;
    }

}