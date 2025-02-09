package com.restaurante.infra.repository.postgres;


import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.DiaEnum;
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
import java.util.List;
import java.util.Optional;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FuncionamentoRepositoryIT {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FuncionamentoRepository repository;

    @Autowired
    private RestauranteRepository restauranteRepository;

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

    @Test
    void devePermitirCriarTabela() {
        long totalTabelasCriada = repository.count();
        assertThat(totalTabelasCriada).isNotNegative();
    }


    @Nested
    class ValidarFuncionamentoRepositoryOT {
        @Test
        void testValidarDatawhenDataIsValid() {
            var diaAtual = LocalDate.now().getDayOfWeek().getValue();
            var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            var funcionamentoEntity = getRandom(FuncionamentoEntity.class);
            funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaAtual));
            funcionamentoEntity.setRestauranteId(restauranteEntity.getId());
            funcionamentoEntity.setAbertura(LocalTime.of(8, 0));
            funcionamentoEntity.setFechamento(LocalTime.of(18, 0));
            var funcionamentoSaved = repository.save(funcionamentoEntity);

            var dataReserva = LocalTime.of(8, 0, 1);

            var dataValidada = repository.validarData(funcionamentoSaved.getRestauranteId(), dataReserva,
                    funcionamentoSaved.getDiaEnum().name());
            assertFalse(dataValidada.isEmpty());
        }

        @Test
        void testValidarDatawhenDataIsInvalid() {
            var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            var funcionamentoEntity = getRandom(FuncionamentoEntity.class);
            funcionamentoEntity.setRestauranteId(restauranteEntity.getId());
            funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(LocalDate.now().getDayOfWeek().getValue()));
            funcionamentoEntity.setAbertura(LocalTime.of(8, 0));
            funcionamentoEntity.setFechamento(LocalTime.of(18, 0));
            var funcionamentoSaved = repository.save(funcionamentoEntity);

            var dataReserva = LocalDateTime.of(LocalDate.now(),
                    funcionamentoSaved.getFechamento().plusHours(1)).toLocalTime();

            var dataValidada = repository.validarData(funcionamentoSaved.getId(),
                    dataReserva, funcionamentoSaved.getDiaEnum().name());
            assertTrue(dataValidada.isEmpty());
        }

        @Test
        void testBuscarTodosFuncionamentosPorRestauranteId() {
            var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));

            var diaAtual = LocalDate.now().getDayOfWeek().getValue();
            FuncionamentoEntity funcionamento1 = FuncionamentoEntity.builder()
                    .diaEnum(DiaEnum.fromInt(LocalDate.now().getDayOfWeek().getValue()))
                    .abertura(LocalTime.of(8, 0))
                    .fechamento(LocalTime.of(22, 0))
                    .restauranteId(restauranteEntity.getId())
                    .build();

            var diaSeguinte = LocalDate.now().plusDays(1).getDayOfWeek().getValue();
            FuncionamentoEntity funcionamento2 = FuncionamentoEntity.builder()
                    .diaEnum(DiaEnum.fromInt(diaSeguinte))
                    .abertura(LocalTime.of(8, 0))
                    .fechamento(LocalTime.of(22, 0))
                    .restauranteId(restauranteEntity.getId())
                    .build();

            var funcionamentoSaved = repository.saveAll(List.of(funcionamento1, funcionamento2));
            var resultado = repository.findAllByRestauranteId(funcionamentoSaved.get(0).getRestauranteId());

            assertThat(resultado.size()).isEqualTo(2);
            assertThat(resultado.get(0).getDiaEnum()).isEqualTo(DiaEnum.fromInt(diaAtual));
            assertThat(resultado.get(1).getDiaEnum()).isEqualTo(DiaEnum.fromInt(diaSeguinte));
        }
    }


    @Nested
    class SalvarFuncionamentoRepositoryIT {
        @Test
        void testSalvarFuncionamento() {
            var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            FuncionamentoEntity funcionamento = getRandom(FuncionamentoEntity.class);
            funcionamento.setRestauranteId(restauranteEntity.getId());
            FuncionamentoEntity savedFuncionamento = repository.save(funcionamento);

            assertNotNull(savedFuncionamento);
            assertThat(savedFuncionamento.getRestauranteId()).isPositive();
            assertThat(savedFuncionamento.getRestauranteId()).isEqualTo(restauranteEntity.getId());
        }
    }

    @Nested
    class BuscarFuncionamentoRepositoryIT {
        @Test
        void testBuscarPorId() {
            var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            FuncionamentoEntity funcionamento = getRandom(FuncionamentoEntity.class);
            funcionamento.setRestauranteId(restauranteEntity.getId());
            FuncionamentoEntity savedFuncionamento = repository.save(funcionamento);

            Optional<FuncionamentoEntity> foundFuncionamento = repository.findById(savedFuncionamento.getId());

            assertTrue(foundFuncionamento.isPresent());
            assertThat(savedFuncionamento.getRestauranteId()).isEqualTo(restauranteEntity.getId());
        }
    }


    @Nested
    class AtualizarFuncionamentoRepositoryIT {
        @Test
        void testAtualizarFuncionamento() {
            var diaSeguinte = LocalDate.now().plusDays(1).getDayOfWeek().getValue();
            var diaAtual = LocalDate.now().getDayOfWeek().getValue();

            var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));

            FuncionamentoEntity funcionamento = getRandom(FuncionamentoEntity.class);
            funcionamento.setRestauranteId(restauranteEntity.getId());
            funcionamento.setDiaEnum(DiaEnum.fromInt(diaAtual));

            FuncionamentoEntity savedFuncionamento = repository.save(funcionamento);
            savedFuncionamento.setDiaEnum(DiaEnum.fromInt(diaSeguinte));
            var funcionamentoAtualizado = repository.save(savedFuncionamento);

            assertNotEquals(funcionamento.getDiaEnum(), funcionamentoAtualizado.getDiaEnum());
            assertEquals(savedFuncionamento.getAbertura(), funcionamentoAtualizado.getAbertura());
            assertEquals(savedFuncionamento.getFechamento(), funcionamentoAtualizado.getFechamento());
        }
    }

    @Nested
    class DeletarFuncionamentoRepositoryIT {
        @Test
        void testDeletarFuncionamento() {
            var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            FuncionamentoEntity funcionamento = getRandom(FuncionamentoEntity.class);
            funcionamento.setRestauranteId(restauranteEntity.getId());
            FuncionamentoEntity savedFuncionamento = repository.save(funcionamento);
            repository.deleteById(savedFuncionamento.getId());
            var result = repository.findById(savedFuncionamento.getId());
            assertTrue(result.isEmpty());
        }
    }
}
