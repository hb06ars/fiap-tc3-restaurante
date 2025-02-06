package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.exceptions.ReservaException;
import com.restaurante.infra.repository.postgres.FuncionamentoRepository;
import com.restaurante.infra.repository.postgres.MesaRepository;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
import com.restaurante.utils.BaseUnitTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ValidaDataUseCaseImplIT extends BaseUnitTest {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ValidarDataUseCaseImpl validaDataUseCase;
    @Autowired
    private FuncionamentoRepository funcionamentoRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MesaRepository mesaRepository;

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
    class ValidarDaraUseCaseIT {
        @Test
        void testExecuteSuccess() {
            LocalDateTime dataReserva = LocalDate.now().atTime(10, 0);
            LocalDate diaSelecionado = LocalDate.now();
            usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(10);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            var mesaEntity = getRandom(MesaEntity.class);
            var diaAtual = LocalDate.now().getDayOfWeek().getValue();
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            mesaRepository.save(mesaEntity);
            FuncionamentoEntity funcionamentoEntity = getRandom(FuncionamentoEntity.class);
            funcionamentoEntity.setRestauranteId(restauranteSaved.getId());
            funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaAtual));
            funcionamentoEntity.setAbertura(LocalTime.of(8, 0, 0));
            funcionamentoEntity.setFechamento(LocalTime.of(22, 0, 0));
            funcionamentoRepository.save(funcionamentoEntity);

            validaDataUseCase.execute(1L, dataReserva, diaSelecionado);
        }

        @Test
        void testExecuteDataInvalida() {
            LocalDateTime dataReserva = LocalDate.now().atTime(9, 0);
            LocalDate diaSelecionado = LocalDate.now();
            assertThrows(ReservaException.class, () -> validaDataUseCase
                    .execute(1L, dataReserva, diaSelecionado));
        }

        @Test
        void testBuscaDiaDaSemanaFeriado() {
            LocalDate diaSelecionado = LocalDate.of(2025, 12, 25);
            DiaEnum diaEnum = validaDataUseCase.buscaDiaDaSemana(diaSelecionado);
            assertEquals(DiaEnum.FERIADOS, diaEnum);
        }

        @Test
        void testBuscaDiaDaSemanaSemana() {
            LocalDate diaSelecionado = LocalDate.of(2025, 1, 27);
            DiaEnum diaEnum = validaDataUseCase.buscaDiaDaSemana(diaSelecionado);
            assertEquals(DiaEnum.SEGUNDA, diaEnum);
        }
    }
}
