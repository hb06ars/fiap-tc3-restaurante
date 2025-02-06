package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.domain.util.DataFormat;
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
import java.time.LocalDateTime;
import java.util.List;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservaRepositoryIT {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
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

    @Nested
    class BuscarReservaRepositoryIT {
        @Test
        void testBuscarReservasPorFiltro() {
            var agora = LocalDateTime.now();
            UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
            ReservaEntity reservaEntity = getRandom(ReservaEntity.class);

            reservaEntity.setUsuarioId(usuarioSaved.getId());
            reservaEntity.setMesaId(mesaSaved.getId());
            reservaEntity.setRestauranteId(restauranteSaved.getId());
            reservaEntity.setDataDaReserva(agora);
            reservaEntity.setStatusPagamento(StatusPagamentoEnum.PENDENTE);
            reservaEntity.setStatusReserva(StatusReservaEnum.OCUPADO);
            reservaEntity.setDataFimReserva(agora.plusHours(2));

            var reservaSaved = reservaRepository.save(reservaEntity);

            List<ReservaEntity> resultado = reservaRepository.findAllByFilter(
                    reservaSaved.getRestauranteId(),
                    reservaSaved.getStatusReserva(),
                    reservaSaved.getStatusPagamento(),
                    DataFormat.truncate(reservaSaved.getDataDaReserva().plusMinutes(10)));

            assertNotNull(resultado);
            assertThat(resultado.size()).isPositive();
        }

        @Test
        void testBuscarPorId() {
            UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
            ReservaEntity reservaSaved = getRandom(ReservaEntity.class);

            reservaSaved.setUsuarioId(usuarioSaved.getId());
            reservaSaved.setMesaId(mesaSaved.getId());
            reservaSaved.setRestauranteId(restauranteSaved.getId());
            reservaSaved.setDataDaReserva(LocalDateTime.now());
            reservaSaved.setDataFimReserva(LocalDateTime.now().plusHours(2));

            assertNotNull(reservaSaved);
            assertThat(reservaSaved.getId()).isPositive();

        }
    }


    @Nested
    class SalvarReservaRepositoryIT {
        @Test
        void testSalvarReserva() {
            UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
            ReservaEntity reservaEntity = getRandom(ReservaEntity.class);

            reservaEntity.setUsuarioId(usuarioSaved.getId());
            reservaEntity.setMesaId(mesaSaved.getId());
            reservaEntity.setRestauranteId(restauranteSaved.getId());

            var reservaSaved = reservaRepository.save(reservaEntity);
            assertNotNull(reservaSaved);
            assertThat(reservaSaved.getId()).isPositive();
        }
    }

    @Nested
    class DeletarReservaRepositoryIT {
        @Test
        void testDeletarReserva() {
            UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
            RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
            MesaEntity mesaEntity = getRandom(MesaEntity.class);
            mesaEntity.setRestauranteId(restauranteSaved.getId());
            MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
            ReservaEntity entity = getRandom(ReservaEntity.class);
            entity.setUsuarioId(usuarioSaved.getId());
            entity.setMesaId(mesaSaved.getId());
            entity.setRestauranteId(restauranteSaved.getId());
            var reservaSaved = reservaRepository.save(entity);

            reservaRepository.deleteById(1L);
            var result = reservaRepository.findById(reservaSaved.getId());
            assertFalse(result.isPresent());
        }
    }
}
