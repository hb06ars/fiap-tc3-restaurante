package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.AvaliacaoEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AvaliacaoRepositoryTest {

    @Mock
    private AvaliacaoRepository repository;

    AutoCloseable openMocks;

    private AvaliacaoEntity avaliacaoEntity;

    @BeforeEach
    void setup() {
        openMocks = MockitoAnnotations.openMocks(this);
        avaliacaoEntity = new AvaliacaoEntity();
        avaliacaoEntity.setId(1L);
        avaliacaoEntity.setRestauranteId(1L);
        avaliacaoEntity.setComentario("Ótima comida!");
        avaliacaoEntity.setNota(5);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testFindAllByRestauranteId() {
        Long restauranteId = 1L;
        AvaliacaoEntity avaliacao1 = new AvaliacaoEntity();
        avaliacao1.setId(1L);
        avaliacao1.setRestauranteId(restauranteId);

        AvaliacaoEntity avaliacao2 = new AvaliacaoEntity();
        avaliacao2.setId(2L);
        avaliacao2.setRestauranteId(restauranteId);

        List<AvaliacaoEntity> avaliacoes = new ArrayList<>();
        avaliacoes.add(avaliacao1);
        avaliacoes.add(avaliacao2);

        when(repository.findAllByRestauranteId(any())).thenReturn(avaliacoes);

        List<AvaliacaoEntity> resultado = repository.findAllByRestauranteId(restauranteId);

        assertEquals(2, resultado.size());
        assertEquals(restauranteId, resultado.get(0).getRestauranteId());
        assertEquals(restauranteId, resultado.get(1).getRestauranteId());
    }

    @Test
    void testSalvarAvaliacao() {
        when(repository.save(any(AvaliacaoEntity.class))).thenReturn(avaliacaoEntity);
        AvaliacaoEntity savedAvaliacao = repository.save(avaliacaoEntity);

        assertNotNull(savedAvaliacao);
        assertEquals(1L, savedAvaliacao.getId());
        assertEquals(1L, savedAvaliacao.getRestauranteId());
        assertEquals("Ótima comida!", savedAvaliacao.getComentario());
        assertEquals(5, savedAvaliacao.getNota());

        verify(repository, times(1)).save(avaliacaoEntity);
    }

    @Test
    void testBuscarPorId() {
        when(repository.findById(any())).thenReturn(Optional.of(avaliacaoEntity));
        Optional<AvaliacaoEntity> foundAvaliacao = repository.findById(1L);

        assertTrue(foundAvaliacao.isPresent());
        assertEquals(1L, foundAvaliacao.get().getId());

        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testBuscarTodasPorRestauranteId() {
        AvaliacaoEntity avaliacao2 = new AvaliacaoEntity();
        avaliacao2.setId(2L);
        avaliacao2.setRestauranteId(1L);
        avaliacao2.setComentario("Bom atendimento!");
        avaliacao2.setNota(4);
        List<AvaliacaoEntity> avaliacoes = Arrays.asList(avaliacaoEntity, avaliacao2);

        when(repository.findAllByRestauranteId(any())).thenReturn(avaliacoes);
        List<AvaliacaoEntity> resultado = repository.findAllByRestauranteId(1L);

        assertEquals(2, resultado.size());
        assertEquals("Ótima comida!", resultado.get(0).getComentario());
        assertEquals("Bom atendimento!", resultado.get(1).getComentario());
        verify(repository, times(1)).findAllByRestauranteId(1L);
    }

    @Test
    void testAtualizarAvaliacao() {
        AvaliacaoEntity avaliacaoAtualizada = new AvaliacaoEntity();
        avaliacaoAtualizada.setId(1L);
        avaliacaoAtualizada.setRestauranteId(1L);
        avaliacaoAtualizada.setComentario("Excelente comida!");
        avaliacaoAtualizada.setNota(5);

        when(repository.save(any(AvaliacaoEntity.class))).thenReturn(avaliacaoAtualizada);
        AvaliacaoEntity updatedAvaliacao = repository.save(avaliacaoAtualizada);
        assertEquals("Excelente comida!", updatedAvaliacao.getComentario());
        verify(repository, times(1)).save(avaliacaoAtualizada);
    }

    @Test
    void testDeletarAvaliacao() {
        doNothing().when(repository).deleteById(any());
        repository.deleteById(1L);
        verify(repository, times(1)).deleteById(1L);
    }


}
