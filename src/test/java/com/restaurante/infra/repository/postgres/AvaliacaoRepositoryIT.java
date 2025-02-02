//package com.restaurante.infra.repository.postgres;
//
//import io.restassured.RestAssured;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.web.server.LocalServerPort;
//import org.springframework.http.HttpStatus;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.transaction.annotation.Transactional;
//
//import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
//import static org.hamcrest.Matchers.equalTo;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@ActiveProfiles("test")
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
//@Transactional
//class AvaliacaoRepositoryIT {
//
//    @LocalServerPort
//    private int port;
//
//    @BeforeEach
//    public void setup() {
//        RestAssured.port = port;
//        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//    }
//
//    @Test
//    void deveAplicacaoIniciarCorretamente() {
//        given()
//                .when()
//                .get("/actuator/health")
//                .then()
//                .statusCode(HttpStatus.OK.value())
//                .body("status", equalTo("UP"));
//    }
//}
