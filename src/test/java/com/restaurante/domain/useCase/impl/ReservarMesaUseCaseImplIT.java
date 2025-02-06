package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.infra.repository.postgres.FuncionamentoRepository;
import com.restaurante.infra.repository.postgres.MesaRepository;
import com.restaurante.infra.repository.postgres.ReservaRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservarMesaUseCaseImplIT extends BaseUnitTest {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservarMesaUseCaseImpl reservarMesaUseCase;
    @Autowired
    private FuncionamentoRepository funcionamentoRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ReservaRepository reservaRepository;
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
    class SalvarAoReservarMesaUseCaseIT {
        @Test
        void salvarDeveChamarValidacoesESalvarReserva() {
            UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(10);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            var mesaEntity = getRandom(MesaEntity.class);
            var diaAtual = LocalDate.now().getDayOfWeek().getValue();
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
            FuncionamentoEntity funcionamentoEntity = getRandom(FuncionamentoEntity.class);
            funcionamentoEntity.setRestauranteId(restauranteSaved.getId());
            funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaAtual));
            funcionamentoEntity.setAbertura(LocalTime.of(8, 0, 0));
            funcionamentoEntity.setFechamento(LocalTime.of(22, 0, 0));

            funcionamentoRepository.save(funcionamentoEntity);

            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setRestauranteId(restauranteSaved.getId());
            dto.setMesaId(mesaSaved.getId());
            dto.setUsuarioId(usuarioSaved.getId());
            dto.setDataDaReserva(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
                    LocalDateTime.now().getDayOfMonth(), 10, 0, 0));
            dto.setDataFimReserva(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
                    LocalDateTime.now().getDayOfMonth(), 12, 0, 0));

            var result = reservarMesaUseCase.salvar(dto);
            assertThat(result.getId()).isPositive();
        }
    }

    @Nested
    class AtualizarAoReservarMesaUseCaseIT {
        @Test
        void atualizarDeveAtualizarDadosDaReservaExistente() {
            var diaAtual = LocalDate.now().getDayOfWeek().getValue();

            UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(10);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            var mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

            FuncionamentoEntity funcionamentoEntity = getRandom(FuncionamentoEntity.class);
            funcionamentoEntity.setRestauranteId(restauranteSaved.getId());
            funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaAtual));
            funcionamentoEntity.setAbertura(LocalTime.of(8, 0, 0));
            funcionamentoEntity.setFechamento(LocalTime.of(22, 0, 0));
            funcionamentoRepository.save(funcionamentoEntity);

            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setRestauranteId(restauranteSaved.getId());
            dto.setMesaId(mesaSaved.getId());
            dto.setUsuarioId(usuarioSaved.getId());
            dto.setDataDaReserva(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
                    LocalDateTime.now().getDayOfMonth(), 10, 0, 0));
            dto.setDataFimReserva(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
                    LocalDateTime.now().getDayOfMonth(), 12, 0, 0));

            var result = reservarMesaUseCase.salvar(dto);
            var registroAtualizado = reservaRepository.findById(result.getId()).orElse(null);
            assert registroAtualizado != null;
            registroAtualizado.setStatusReserva(StatusReservaEnum.CANCELADO);
            var resultB = reservarMesaUseCase.atualizar(registroAtualizado.getId(), new ReservaDTO(registroAtualizado));
            assertThat(resultB.getStatusReserva()).isEqualTo(StatusReservaEnum.CANCELADO);
        }
    }

    @Nested
    class PreencherHorarioSaidaAoReservarMesaUseCaseIT {
        @Test
        void preencherHorarioDeSaidaDeveDefinirHorarioDeSaidaCorreto() {
            var diaAtual = LocalDate.now().getDayOfWeek().getValue();

            UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(10);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            var mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
            FuncionamentoEntity funcionamentoEntity = getRandom(FuncionamentoEntity.class);
            funcionamentoEntity.setRestauranteId(restauranteSaved.getId());
            funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaAtual));
            funcionamentoEntity.setAbertura(LocalTime.of(8, 0, 0));
            funcionamentoEntity.setFechamento(LocalTime.of(22, 0, 0));
            funcionamentoRepository.save(funcionamentoEntity);

            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setRestauranteId(restauranteSaved.getId());
            dto.setMesaId(mesaSaved.getId());
            dto.setUsuarioId(usuarioSaved.getId());
            dto.setDataDaReserva(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
                    LocalDateTime.now().getDayOfMonth(), 10, 0, 0));
            dto.setDataFimReserva(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(),
                    LocalDateTime.now().getDayOfMonth(), 12, 0, 0));

            reservarMesaUseCase.salvar(dto);
            assertEquals(dto.getDataDaReserva().plusHours(2), dto.getDataFimReserva());
        }
    }

}
