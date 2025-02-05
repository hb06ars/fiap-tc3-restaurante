package com.restaurante.app.rest.controller;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.infra.repository.postgres.FuncionamentoRepository;
import com.restaurante.infra.repository.postgres.MesaRepository;
import com.restaurante.infra.repository.postgres.ReservaRepository;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservaControllerIT extends BaseUnitTest {

    @LocalServerPort
    private int port;

    @Autowired
    MesaRepository mesaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    FuncionamentoRepository funcionamentoRepository;
    @Autowired
    RestauranteRepository restauranteRepository;
    @Autowired
    ReservaRepository reservaRepository;
    int diaSemanaAtual;


    @BeforeEach
    public void setup() {
        diaSemanaAtual = LocalDate.now().getDayOfWeek().getValue();
        RestAssured.port = port > 0 ? port : 8080;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void testCadastrarReserva() {
        var request = getReservaEntity();

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/reserva")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasKey("id"))
                .body("$", hasKey("usuarioId"))
                .body("$", hasKey("mesaId"))
                .body("$", hasKey("restauranteId"));
    }


    @Test
    void testAtualizarReserva() {
        var reservaEntity = getReservaEntity();
        var reservaSaved = reservaRepository.save(reservaEntity);

        reservaSaved.setStatusReserva(StatusReservaEnum.RESERVADO);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservaSaved)
                .when()
                .put("/reserva/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasKey("id"))
                .body("$", hasKey("usuarioId"))
                .body("$", hasKey("mesaId"))
                .body("$", hasKey("restauranteId"));
    }

    @Test
    void testBuscarReservas() {
        var reservaEntity = getReservaEntity();
        reservaRepository.save(reservaEntity);
        ReservaDTO bodyStr = new ReservaDTO();
        bodyStr.setDataDaReserva(LocalDate.now().atTime(10, 0));
        bodyStr.setStatusPagamento(StatusPagamentoEnum.PENDENTE);
        bodyStr.setStatusReserva(StatusReservaEnum.OCUPADO);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .body(bodyStr)
                .get("/reserva/1")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("[0]", hasKey("id"))
                .body("[0]", hasKey("usuarioId"))
                .body("[0]", hasKey("mesaId"))
                .body("[0]", hasKey("restauranteId"))
                .body("[0]", hasKey("dataDaReserva"))
                .body("[0]", hasKey("dataFimReserva"))
                .body("[0]", hasKey("valorReserva"))
                .body("[0]", hasKey("statusPagamento"))
                .body("[0]", hasKey("statusReserva"));
    }

    @Test
    void testAtualizarExcecaoQuandoIdNaoEncontrado() {
        ReservaEntity reservaEntity = getRandom(ReservaEntity.class);
        reservaEntity.setRestauranteId(1L);
        reservaEntity.setUsuarioId(1L);
        reservaEntity.setMesaId(1L);

        given()
                .filter(new AllureRestAssured())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservaEntity)
                .when()
                .put("/reserva/9")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .body("message[0].erro", equalTo("Erro no sistema"));
    }

    private ReservaEntity getReservaEntity() {
        RestauranteEntity restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(5);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);

        var mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setId(1L);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        mesaEntity.setNomeMesa("MESA 1");
        mesaRepository.save(mesaEntity);

        var mesaEntityb = getRandom(MesaEntity.class);
        mesaEntityb.setId(2L);
        mesaEntityb.setRestauranteId(restauranteSaved.getId());
        mesaEntityb.setNomeMesa("MESA 2");
        mesaRepository.save(mesaEntityb);

        FuncionamentoEntity funcionamentoEntity = getRandom(FuncionamentoEntity.class);
        funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaSemanaAtual));
        funcionamentoEntity.setAbertura(LocalTime.of(8, 0));
        funcionamentoEntity.setFechamento(LocalTime.of(18, 0));
        funcionamentoEntity.setRestauranteId(restauranteSaved.getId());
        funcionamentoRepository.save(funcionamentoEntity);

        UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));

        var request = getRandom(ReservaEntity.class);
        request.setDataDaReserva(LocalDate.now().atTime(10, 0));
        request.setDataFimReserva(LocalDate.now().atTime(12, 0));
        request.setRestauranteId(restauranteSaved.getId());
        request.setUsuarioId(usuarioSaved.getId());
        request.setStatusReserva(StatusReservaEnum.OCUPADO);
        request.setStatusPagamento(StatusPagamentoEnum.PENDENTE);
        request.setValorReserva(BigDecimal.valueOf(100));
        request.setMesaId(1L);
        return request;
    }

}