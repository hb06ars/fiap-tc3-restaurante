package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.AvaliacaoEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class AvaliacaoRepositoryIT extends BaseUnitTest {

    @Autowired
    private AvaliacaoRepository repository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void devePermitirCriarTabela() {
        long totalTabelasCriada = repository.count();
        assertThat(totalTabelasCriada).isNotNegative();
    }

    @Test
    void testFindAllByRestauranteId() {
        var avaliacaoEntity = getRandom(AvaliacaoEntity.class);
        var usuarioSalvo = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteSalvo = restauranteRepository.save(getRandom(RestauranteEntity.class));
        avaliacaoEntity.setUsuarioId(usuarioSalvo.getId());
        avaliacaoEntity.setNota(5);
        avaliacaoEntity.setRestauranteId(restauranteSalvo.getId());

        repository.save(avaliacaoEntity);
        List<AvaliacaoEntity> resultado = repository.findAllByRestauranteId(restauranteSalvo.getId());

        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isPositive();
        assertThat(restauranteSalvo.getId()).isEqualTo(resultado.get(0).getRestauranteId());
    }

    @Test
    void testSalvarAvaliacao() {
        var avaliacaoEntity = getRandom(AvaliacaoEntity.class);
        var usuarioSalvo = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteSalvo = restauranteRepository.save(getRandom(RestauranteEntity.class));
        avaliacaoEntity.setUsuarioId(usuarioSalvo.getId());
        avaliacaoEntity.setNota(5);
        avaliacaoEntity.setRestauranteId(restauranteSalvo.getId());

        var savedAvaliacao = repository.save(avaliacaoEntity);

        assertThat(savedAvaliacao).isNotNull();
        assertThat(savedAvaliacao.getId()).isPositive();
    }

    @Test
    void testBuscarPorId() {
        var avaliacaoEntity = getRandom(AvaliacaoEntity.class);
        var usuarioSalvo = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteSalvo = restauranteRepository.save(getRandom(RestauranteEntity.class));
        avaliacaoEntity.setUsuarioId(usuarioSalvo.getId());
        avaliacaoEntity.setNota(5);
        avaliacaoEntity.setRestauranteId(restauranteSalvo.getId());

        var savedAvaliacao = repository.save(avaliacaoEntity);

        Optional<AvaliacaoEntity> avaliacaoEncontrada = repository.findById(savedAvaliacao.getId());

        assertTrue(avaliacaoEncontrada.isPresent());
        assertThat(avaliacaoEncontrada.get().getId()).isEqualTo(savedAvaliacao.getId());
    }

    @Test
    void testAtualizarAvaliacao() {
        var avaliacaoEntity = getRandom(AvaliacaoEntity.class);
        var usuarioSalvo = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteSalvo = restauranteRepository.save(getRandom(RestauranteEntity.class));
        avaliacaoEntity.setUsuarioId(usuarioSalvo.getId());
        avaliacaoEntity.setNota(5);
        avaliacaoEntity.setRestauranteId(restauranteSalvo.getId());

        var savedAvaliacao = repository.save(avaliacaoEntity);
        savedAvaliacao.setComentario("Alteração do comentário OK.");
        var valorAtualizado = repository.save(savedAvaliacao);

        assertThat(valorAtualizado.getId()).isEqualTo(savedAvaliacao.getId());
        assertThat(valorAtualizado.getComentario()).isEqualTo("Alteração do comentário OK.");
    }

    @Test
    void testDeletarAvaliacao() {
        var avaliacaoEntity = getRandom(AvaliacaoEntity.class);
        var usuarioSalvo = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteSalvo = restauranteRepository.save(getRandom(RestauranteEntity.class));
        avaliacaoEntity.setUsuarioId(usuarioSalvo.getId());
        avaliacaoEntity.setNota(5);
        avaliacaoEntity.setRestauranteId(restauranteSalvo.getId());

        var savedAvaliacao = repository.save(avaliacaoEntity);
        repository.deleteById(savedAvaliacao.getId());
        var result = repository.findById(savedAvaliacao.getId()).isPresent();

        assertFalse(result);
    }

}
