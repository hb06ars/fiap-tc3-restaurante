package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.domain.entity.AvaliacaoEntity;
import com.restaurante.infra.repository.postgres.AvaliacaoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AvaliacaoServiceTest {

    AutoCloseable openMocks;

    @InjectMocks
    private AvaliacaoService avaliacaoService;

    @Mock
    private AvaliacaoRepository avaliacaoRepository;

    private AvaliacaoDTO avaliacaoDTO;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        avaliacaoDTO = new AvaliacaoDTO();
        avaliacaoDTO.setId(1L);
        avaliacaoDTO.setComentario("Ótimo Restaurante!");
        avaliacaoDTO.setNota(5);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testSave() {
        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity(avaliacaoDTO);
        when(avaliacaoRepository.save(any(AvaliacaoEntity.class))).thenReturn(avaliacaoEntity);

        AvaliacaoDTO savedAvaliacao = avaliacaoService.save(avaliacaoDTO);

        assertNotNull(savedAvaliacao);
        assertEquals(avaliacaoDTO.getId(), savedAvaliacao.getId());
        verify(avaliacaoRepository, times(1)).save(any(AvaliacaoEntity.class));
    }

    @Test
    void testFindAll() {
        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity(avaliacaoDTO);
        when(avaliacaoRepository.findAll()).thenReturn(List.of(avaliacaoEntity));

        List<AvaliacaoDTO> avaliacaoList = avaliacaoService.findAll();

        assertNotNull(avaliacaoList);
        assertEquals(1, avaliacaoList.size());
        verify(avaliacaoRepository, times(1)).findAll();
    }

    @Test
    void testFindById_Found() {
        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity(avaliacaoDTO);
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacaoEntity));

        AvaliacaoDTO foundAvaliacao = avaliacaoService.findById(1L);

        assertNotNull(foundAvaliacao);
        assertEquals(avaliacaoDTO.getId(), foundAvaliacao.getId());
        verify(avaliacaoRepository, times(1)).findById(1L);
    }

    @Test
    void testFindById_NotFound() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        AvaliacaoDTO foundAvaliacao = avaliacaoService.findById(1L);

        assertNull(foundAvaliacao);
        verify(avaliacaoRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdate_Success() {
        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity(avaliacaoDTO);
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacaoEntity));
        when(avaliacaoRepository.save(any(AvaliacaoEntity.class))).thenReturn(avaliacaoEntity);

        AvaliacaoDTO updatedAvaliacao = avaliacaoService.update(1L, avaliacaoDTO);

        assertNotNull(updatedAvaliacao);
        assertEquals(avaliacaoDTO.getId(), updatedAvaliacao.getId());
        verify(avaliacaoRepository, times(1)).findById(1L);
        verify(avaliacaoRepository, times(1)).save(any(AvaliacaoEntity.class));
    }

    @Test
    void testUpdate_NotFound() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> avaliacaoService.update(1L, avaliacaoDTO));

        verify(avaliacaoRepository, times(1)).findById(1L);
    }

    @Test
    void testDelete_Success() {
        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity(avaliacaoDTO);
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.of(avaliacaoEntity));
        avaliacaoService.delete(1L);
        verify(avaliacaoRepository, times(2)).findById(1L);
    }

    @Test
    void testDelete_NotFound() {
        when(avaliacaoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> avaliacaoService.delete(1L));

        verify(avaliacaoRepository, times(1)).findById(1L);
    }

    @Test
    void testListarPorRestaurante() {
        AvaliacaoEntity avaliacaoEntity = new AvaliacaoEntity(avaliacaoDTO);
        when(avaliacaoRepository.findAllByRestauranteId(1L)).thenReturn(List.of(avaliacaoEntity));

        List<AvaliacaoDTO> avaliacaoList = avaliacaoService.listarPorRestaurante(1L);

        assertNotNull(avaliacaoList);
        assertEquals(1, avaliacaoList.size());
        verify(avaliacaoRepository, times(1)).findAllByRestauranteId(1L);
    }
}
