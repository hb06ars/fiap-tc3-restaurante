package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.AvaliacaoEntity;
import com.restaurante.utils.RegistroHelper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class MensagemRepositoryIT {

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
        Long restauranteId = 1L;
        gerarRegistros();

        List<AvaliacaoEntity> resultado = repository.findAllByRestauranteId(restauranteId);

        assertEquals(2, resultado.size());
        assertEquals(restauranteId, resultado.get(0).getRestauranteId());
        assertEquals(restauranteId, resultado.get(1).getRestauranteId());
    }

    @Test
    void testSalvarAvaliacao() {
        gerarRegistros();
        var avaliacaoEntity = RegistroHelper.gerarAvaliacao();
        avaliacaoEntity.setId(3L);
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
        gerarRegistros();
        Optional<AvaliacaoEntity> foundAvaliacao = repository.findById(1L);

        assertThat(foundAvaliacao.isPresent()).isNotNull();
        assertThat(foundAvaliacao.get().getId()).isEqualTo(1L);
    }

    @Test
    void testAtualizarAvaliacao() {
        gerarRegistros();
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
        gerarRegistros();
        repository.deleteById(1L);
        var result = repository.findById(1L).isPresent();
        assertFalse(result);
    }

    private void gerarRegistros() {
        var usuario = RegistroHelper.gerarUsuario();
        var restaurante = RegistroHelper.gerarRestaurante();
        var avaliacao = RegistroHelper.gerarAvaliacao();

        usuarioRepository.save(usuario);
        usuario.setId(2L);
        usuario.setEmail("novoemail@mail.com");
        usuario.setCelular("11999999555");
        usuarioRepository.save(usuario);

        restauranteRepository.save(restaurante);
        repository.save(avaliacao);

        avaliacao.setUsuarioId(2L);
        avaliacao.setId(2L);

        repository.save(avaliacao);
    }
}
