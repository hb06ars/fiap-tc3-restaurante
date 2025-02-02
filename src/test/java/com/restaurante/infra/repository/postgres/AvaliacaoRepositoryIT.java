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
        var usuarioEntity = getRandom(UsuarioEntity.class);
        var restauranteEntity = getRandom(RestauranteEntity.class);
        var avaliacaoEntity = getRandom(AvaliacaoEntity.class);

        usuarioRepository.save(usuarioEntity);
        restauranteRepository.save(restauranteEntity);
        AvaliacaoEntity savedAvaliacao = repository.save(avaliacaoEntity);

        assertThat(savedAvaliacao).isNotNull();
        assertThat(avaliacaoEntity.getId()).isEqualTo(savedAvaliacao.getId());
        assertThat(avaliacaoEntity.getRestauranteId()).isEqualTo(savedAvaliacao.getRestauranteId());
        assertThat(avaliacaoEntity.getComentario()).isEqualTo(savedAvaliacao.getComentario());
        assertThat(avaliacaoEntity.getNota()).isEqualTo(savedAvaliacao.getNota());

        assertThat(savedAvaliacao).isNotNull();
    }

    @Test
    void testBuscarPorId() {
        var usuarioEntity = getRandom(UsuarioEntity.class);
        var restauranteEntity = getRandom(RestauranteEntity.class);
        var avaliacaoEntity = getRandom(AvaliacaoEntity.class);

        usuarioRepository.save(usuarioEntity);
        restauranteRepository.save(restauranteEntity);
        repository.save(avaliacaoEntity);

        Optional<AvaliacaoEntity> foundAvaliacao = repository.findById(1L);

        assertThat(foundAvaliacao.isPresent()).isNotNull();
        assertThat(foundAvaliacao.get().getId()).isEqualTo(1L);
    }

    @Test
    void testAtualizarAvaliacao() {
        var usuarioEntity = getRandom(UsuarioEntity.class);
        var restauranteEntity = getRandom(RestauranteEntity.class);
        var avaliacaoEntity = getRandom(AvaliacaoEntity.class);

        usuarioRepository.save(usuarioEntity);
        restauranteRepository.save(restauranteEntity);
        repository.save(avaliacaoEntity);

        AvaliacaoEntity avaliacaoAtualizada = new AvaliacaoEntity();
        avaliacaoAtualizada.setId(1L);
        avaliacaoAtualizada.setRestauranteId(1L);
        avaliacaoAtualizada.setComentario("Excelente comida!");
        avaliacaoAtualizada.setNota(5);

        repository.save(avaliacaoAtualizada);
        avaliacaoAtualizada.setComentario("Muito bom.");
        repository.save(avaliacaoAtualizada);

        assertThat(avaliacaoAtualizada.getId()).isEqualTo(1L);
        assertThat(avaliacaoAtualizada.getComentario()).isEqualTo("Muito bom.");
    }

    @Test
    void testDeletarAvaliacao() {
        var usuarioEntity = getRandom(UsuarioEntity.class);
        var restauranteEntity = getRandom(RestauranteEntity.class);
        var avaliacaoEntity = getRandom(AvaliacaoEntity.class);

        usuarioRepository.save(usuarioEntity);
        restauranteRepository.save(restauranteEntity);
        repository.save(avaliacaoEntity);

        repository.deleteById(1L);
        var result = repository.findById(1L).isPresent();
        assertFalse(result);
    }

}
