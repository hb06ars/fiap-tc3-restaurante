package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.infra.repository.postgres.AvaliacaoRepository;
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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AvaliacaoServiceIT extends BaseUnitTest {

    @LocalServerPort
    private int port;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private AvaliacaoService avaliacaoService;
    @Autowired
    private AvaliacaoRepository avaliacaoRepository;
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
    class SalvarAvaliacaoServiceIT {
        @Test
        void testSave() {
            UsuarioEntity usuarioEntity = usuarioRepository.save(getRandom(UsuarioEntity.class));
            RestauranteEntity restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            AvaliacaoDTO dto = getRandom(AvaliacaoDTO.class);
            dto.setNota(5);
            dto.setUsuarioId(usuarioEntity.getId());
            dto.setRestauranteId(restauranteEntity.getId());

            AvaliacaoDTO result = avaliacaoService.save(dto);

            assertNotNull(result);
            assertThat(result.getId()).isPositive();
        }
    }

    @Nested
    class ListarAvaliacaoServiceIT {
        @Test
        void testFindAll() {
            UsuarioEntity usuarioEntity = usuarioRepository.save(getRandom(UsuarioEntity.class));
            RestauranteEntity restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            AvaliacaoDTO dto = getRandom(AvaliacaoDTO.class);
            dto.setNota(5);
            dto.setUsuarioId(usuarioEntity.getId());
            dto.setRestauranteId(restauranteEntity.getId());
            avaliacaoService.save(dto);

            List<AvaliacaoDTO> avaliacaoList = avaliacaoService.findAll();
            assertNotNull(avaliacaoList);
            assertThat(avaliacaoList.size()).isPositive();
        }

        @Test
        void testFindByIdFound() {
            UsuarioEntity usuarioEntity = usuarioRepository.save(getRandom(UsuarioEntity.class));
            RestauranteEntity restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            AvaliacaoDTO dto = getRandom(AvaliacaoDTO.class);
            dto.setNota(5);
            dto.setUsuarioId(usuarioEntity.getId());
            dto.setRestauranteId(restauranteEntity.getId());
            avaliacaoService.save(dto);

            AvaliacaoDTO foundAvaliacao = avaliacaoService.findById(1L);
            assertNotNull(foundAvaliacao);
        }

        @Test
        void testFindByIdNotFound() {
            AvaliacaoDTO foundAvaliacao = avaliacaoService.findById(1L);
            assertNull(foundAvaliacao);
        }

        @Test
        void testListarPorRestaurante() {
            List<AvaliacaoDTO> avaliacaoList = avaliacaoService.listarPorRestaurante(1L);
            assertNotNull(avaliacaoList);
        }

    }

    @Nested
    class AtualizarAvaliacaoServiceIT {
        @Test
        void testUpdateSuccess() {
            UsuarioEntity usuarioEntity = usuarioRepository.save(getRandom(UsuarioEntity.class));
            RestauranteEntity restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            AvaliacaoDTO dto = getRandom(AvaliacaoDTO.class);
            dto.setNota(5);
            dto.setUsuarioId(usuarioEntity.getId());
            dto.setRestauranteId(restauranteEntity.getId());
            var avaliacaoSaved = avaliacaoService.save(dto);

            avaliacaoSaved.setComentario("Alteração");
            AvaliacaoDTO updatedAvaliacao = avaliacaoService.update(avaliacaoSaved.getId(), avaliacaoSaved);

            assertNotNull(updatedAvaliacao);
            assertThat(updatedAvaliacao.getId()).isPositive();
            assertThat(updatedAvaliacao.getComentario()).isEqualTo("Alteração");
        }

        @Test
        void testUpdateNotFound() {
            assertThrows(RuntimeException.class, () -> avaliacaoService.update(1L, null));
        }

    }

    @Nested
    class DeletarAvaliacaoServiceIT {
        @Test
        void testDeleteSuccess() {
            UsuarioEntity usuarioEntity = usuarioRepository.save(getRandom(UsuarioEntity.class));
            RestauranteEntity restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
            AvaliacaoDTO dto = getRandom(AvaliacaoDTO.class);
            dto.setNota(5);
            dto.setUsuarioId(usuarioEntity.getId());
            dto.setRestauranteId(restauranteEntity.getId());
            var saved = avaliacaoService.save(dto);

            avaliacaoService.delete(saved.getId());
            var item = avaliacaoService.findById(saved.getId());

            assertNull(item);
        }

        @Test
        void testDeleteNotFound() {
            assertThrows(RuntimeException.class, () -> avaliacaoService.delete(1L));
        }
    }

}
