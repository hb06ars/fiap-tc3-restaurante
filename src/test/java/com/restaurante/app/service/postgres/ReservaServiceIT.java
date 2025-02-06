package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.infra.exceptions.FieldNotFoundException;
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
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservaServiceIT extends BaseUnitTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private FuncionamentoRepository funcionamentoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ReservaService reservaService;

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
    class SalvarReservaServiceIT {
        @Test
        void saveReturnsReservaDTO() {
            var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(4);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setMesaId(mesaSaved.getId());
            dto.setRestauranteId(restauranteSaved.getId());
            dto.setUsuarioId(usuarioSaved.getId());
            ReservaDTO result = reservaService.save(dto);

            assertNotNull(result);
            assertThat(result.getId()).isPositive();
        }
    }

    @Nested
    class BuscarReservaServiceIT {
        @Test
        void findAllReturnsReservaDTOList() {
            var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(4);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setMesaId(mesaSaved.getId());
            dto.setRestauranteId(restauranteSaved.getId());
            dto.setUsuarioId(usuarioSaved.getId());
            reservaService.save(dto);

            List<ReservaDTO> result = reservaService.findAll();

            assertNotNull(result);
            assertThat(result.size()).isPositive();
        }

        @Test
        void findByIdReturnsReservaDTO() {
            var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(4);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setMesaId(mesaSaved.getId());
            dto.setRestauranteId(restauranteSaved.getId());
            dto.setUsuarioId(usuarioSaved.getId());
            ReservaDTO reservaSaved = reservaService.save(dto);

            ReservaDTO result = reservaService.findById(reservaSaved.getId());

            assertNotNull(result);
            assertThat(result.getId()).isPositive();

        }

        @Test
        void findByIdReturnsNullWhenReservaNotFound() {
            ReservaDTO result = reservaService.findById(1L);
            assertNull(result);
        }
    }

    @Nested
    class ListarReservaServiceIT {
        @Test
        void updateReturnsUpdatedReservaDTO() {
            var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(4);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setMesaId(mesaSaved.getId());
            dto.setRestauranteId(restauranteSaved.getId());
            dto.setUsuarioId(usuarioSaved.getId());
            dto.setStatusPagamento(StatusPagamentoEnum.PENDENTE);

            ReservaDTO reservaSaved = reservaService.save(dto);
            reservaSaved.setStatusPagamento(StatusPagamentoEnum.PAGO);
            ReservaDTO result = reservaService.update(reservaSaved.getId(), reservaSaved);

            assertNotNull(result);
            assertThat(result.getId()).isPositive();
        }

        @Test
        void updateThrowsRuntimeExceptionWhenReservaNotFound() {
            ReservaDTO dto = getRandom(ReservaDTO.class);
            assertThrows(RuntimeException.class, () -> reservaService.update(1L, dto));
        }

        @Test
        void buscarThrowsFieldNotFoundExceptionWhenDataDaReservaIsNull() {
            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setDataDaReserva(null);
            assertThrows(FieldNotFoundException.class, () -> reservaService.buscar(1L, dto));
        }

        @Test
        void buscarReturnsReservaDTOList() {
            var diaAtual = LocalDate.now().getDayOfWeek().getValue();
            var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(4);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
            FuncionamentoEntity funcionamentoEntity = getRandom(FuncionamentoEntity.class);
            funcionamentoEntity.setRestauranteId(restauranteSaved.getId());
            funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaAtual));
            funcionamentoEntity.setAbertura(LocalTime.of(8, 0, 0));
            funcionamentoEntity.setFechamento(LocalTime.of(22, 0, 0));
            funcionamentoRepository.save(funcionamentoEntity);

            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setMesaId(mesaSaved.getId());
            dto.setRestauranteId(restauranteSaved.getId());
            dto.setDataDaReserva(LocalDate.now().atTime(14, 0));
            dto.setDataFimReserva(LocalDate.now().atTime(16, 0));
            dto.setUsuarioId(usuarioSaved.getId());
            ReservaDTO reservaSaved = reservaService.save(dto);

            List<ReservaDTO> result = reservaService.buscar(reservaSaved.getRestauranteId(), reservaSaved);

            assertNotNull(result);
            assertThat(result.size()).isPositive();
        }
    }

    @Nested
    class DeletarReservaServiceIT {
        @Test
        void deleteSuccess() {
            var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            var restauranteEntity = getRandom(RestauranteEntity.class);
            restauranteEntity.setCapacidade(4);
            var restauranteSaved = restauranteRepository.save(restauranteEntity);
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

            ReservaDTO dto = getRandom(ReservaDTO.class);
            dto.setMesaId(mesaSaved.getId());
            dto.setRestauranteId(restauranteSaved.getId());
            dto.setUsuarioId(usuarioSaved.getId());
            ReservaDTO reservaSaved = reservaService.save(dto);

            reservaService.delete(reservaSaved.getId());
            var result = reservaService.findById(reservaSaved.getId());

            assertNull(result);
        }

        @Test
        void deleteThrowsRuntimeExceptionWhenReservaNotFound() {
            assertThrows(RuntimeException.class, () -> reservaService.delete(1L));
        }
    }

}
