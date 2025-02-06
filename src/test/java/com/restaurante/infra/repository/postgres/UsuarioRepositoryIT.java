package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.UsuarioEntity;
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

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioRepositoryIT {
    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

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
    class BuscarUsuarioRepositoryIT {
        @Test
        void testBuscarPorEmailOuCelular() {
            UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);

            var usuarioSaved = usuarioRepository.save(usuarioEntity);
            var result = usuarioRepository.findByEmailOrCelular(usuarioSaved.getEmail(), usuarioSaved.getCelular());

            assertNotNull(result);
            assertThat(result.getId()).isEqualTo(usuarioSaved.getId());
        }

        @Test
        void testBuscarPorId() {
            UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);
            var usuarioSaved = usuarioRepository.save(usuarioEntity);

            var result = usuarioRepository.findById(usuarioSaved.getId());

            assertNotNull(result);
            assertThat(result.get().getId()).isEqualTo(usuarioSaved.getId());
        }
    }

    @Nested
    class SalvarUsuarioRepositoryIT {
        @Test
        void testSalvarUsuario() {
            UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);

            var usuarioSaved = usuarioRepository.save(usuarioEntity);

            assertNotNull(usuarioSaved);
            assertThat(usuarioSaved.getId()).isPositive();
        }

        @Test
        void testAtualizarUsuario() {
            UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);
            usuarioEntity.setNome("Fulano");

            var usuarioSaved = usuarioRepository.save(usuarioEntity);
            usuarioSaved.setNome("Fulano de Tal");
            var result = usuarioRepository.save(usuarioSaved);

            assertNotNull(result);
            assertThat(result.getNome()).isEqualTo("Fulano de Tal");
        }
    }

    @Nested
    class DeletarUsuarioRepositoryIT {
        @Test
        void testDeletarUsuario() {
            UsuarioEntity usuarioEntity = getRandom(UsuarioEntity.class);
            var usuarioSaved = usuarioRepository.save(usuarioEntity);

            usuarioRepository.deleteById(usuarioSaved.getId());
            var result = usuarioRepository.findById(usuarioSaved.getId());

            assertFalse(result.isPresent());
        }
    }
}
