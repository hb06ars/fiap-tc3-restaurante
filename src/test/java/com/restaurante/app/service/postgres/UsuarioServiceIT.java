package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.UsuarioDTO;
import com.restaurante.infra.exceptions.RecordAlreadyExistsException;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UsuarioServiceIT extends BaseUnitTest {

    @LocalServerPort
    private int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UsuarioService usuarioService;

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
    class SalvarUsuarioServiceIT {
        @Test
        void saveReturnsUsuarioDTOWhenUserDoesNotExist() {
            var result = usuarioService.save(getRandom(UsuarioDTO.class));

            assertNotNull(result);
            assertThat(result.getId()).isPositive();
        }

        @Test
        void saveThrowsRecordAlreadyExistsExceptionWhenEmailOrCelularExists() {
            var usuarioSaved = usuarioService.save(getRandom(UsuarioDTO.class));
            assertThrows(RecordAlreadyExistsException.class, () -> usuarioService.save(usuarioSaved));
        }
    }

    @Nested
    class BuscarUSuarioServiceIT {
        @Test
        void findAllReturnsListOfUsuarios() {
            usuarioService.save(getRandom(UsuarioDTO.class));
            List<UsuarioDTO> result = usuarioService.findAll();

            assertNotNull(result);
            assertThat(result.size()).isPositive();

        }

        @Test
        void findByIdReturnsUsuarioDTO() {
            var user = usuarioService.save(getRandom(UsuarioDTO.class));
            UsuarioDTO result = usuarioService.findById(user.getId());

            assertNotNull(result);
            assertThat(result.getId()).isPositive();

        }

        @Test
        void findByEmailOrCelularReturnsUsuarioDTO() {
            var user = usuarioService.save(getRandom(UsuarioDTO.class));
            UsuarioDTO result = usuarioService.findByEmailOrCelular(user.getEmail(), user.getCelular());

            assertNotNull(result);
            assertThat(result.getId()).isPositive();

        }

        @Test
        void findByEmailOrCelularReturnsNullwhenCelularIsNull() {
            assertThrows(RuntimeException.class, () -> usuarioService
                    .findByEmailOrCelular("email@mail.com", null));
        }

        @Test
        void findByEmailOrCelularReturnsNullwhenEmailIsNull() {
            assertThrows(RuntimeException.class, () -> usuarioService
                    .findByEmailOrCelular(null, "123456789"));
        }


        @Test
        void findByIdReturnsNullWhenUsuarioNotFound() {
            UsuarioDTO result = usuarioService.findById(1L);
            assertNull(result);
        }
    }

    @Nested
    class AtualizarUsuarioServiceIT {
        @Test
        void updateReturnsUpdatedUsuarioDTO() {
            var dto = getRandom(UsuarioDTO.class);
            dto.setNome("Nome inicial");
            var saved = usuarioService.save(dto);

            saved.setNome("Teste nome original");
            UsuarioDTO result = usuarioService.update(saved.getId(), saved);

            assertNotNull(result);
            assertThat(result.getId()).isPositive();
        }

        @Test
        void updateThrowsRuntimeExceptionWhenUsuarioNotFound() {
            assertThrows(RuntimeException.class, () -> usuarioService.update(1L, null));
        }

        @Test
        void updateThrowsRuntimeExceptionWhenEmailOrCelularAlreadyExists() {
            assertThrows(RuntimeException.class, () -> usuarioService.update(1L, null));
        }
    }

    @Nested
    class DeletarUsurioServiceIT {
        @Test
        void deleteSuccess() {
            var saved = usuarioService.save(getRandom(UsuarioDTO.class));
            usuarioService.delete(saved.getId());
            var result = usuarioService.findById(saved.getId());
            assertNull(result);
        }

        @Test
        void deleteThrowsRuntimeExceptionWhenUsuarioNotFound() {
            assertThrows(RuntimeException.class, () -> usuarioService.delete(1L));
        }
    }
}
