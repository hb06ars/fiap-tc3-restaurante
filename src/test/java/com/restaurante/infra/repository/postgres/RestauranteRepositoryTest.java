package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.TipoCozinhaEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestauranteRepositoryTest {

    @Mock
    private RestauranteRepository restauranteRepository;

    private RestauranteEntity restaurante;

    AutoCloseable openMocks;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        restaurante = new RestauranteEntity();
        restaurante.setId(1L);
        restaurante.setNome("Bistrô Gourmet");
        restaurante.setLocalizacao("São Paulo");
        restaurante.setTipoCozinha(TipoCozinhaEnum.FRANCESA);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void testSalvarRestaurante() {
        when(restauranteRepository.save(restaurante)).thenReturn(restaurante);

        RestauranteEntity savedRestaurante = restauranteRepository.save(restaurante);

        assertNotNull(savedRestaurante);
        assertEquals(1L, savedRestaurante.getId());
        assertEquals("Bistrô Gourmet", savedRestaurante.getNome());
        assertEquals("São Paulo", savedRestaurante.getLocalizacao());

        verify(restauranteRepository, times(1)).save(restaurante);
    }

    @Test
    void testBuscarPorId() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restaurante));

        Optional<RestauranteEntity> foundRestaurante = restauranteRepository.findById(1L);

        assertTrue(foundRestaurante.isPresent());
        assertEquals(1L, foundRestaurante.get().getId());

        verify(restauranteRepository, times(1)).findById(1L);
    }

    @Test
    void testBuscarRestaurantesPorFiltro() {
        RestauranteEntity restaurante2 = new RestauranteEntity();
        restaurante2.setId(2L);
        restaurante2.setNome("Pizzaria Bella");
        restaurante2.setLocalizacao("São Paulo");
        restaurante2.setTipoCozinha(TipoCozinhaEnum.ITALIANA);

        List<RestauranteEntity> restaurantes = Arrays.asList(restaurante, restaurante2);

        when(restauranteRepository.buscarRestaurantes("Bistrô", "São Paulo", "Francesa"))
                .thenReturn(restaurantes);

        List<RestauranteEntity> resultado = restauranteRepository.buscarRestaurantes("Bistrô", "São Paulo", "Francesa");

        assertFalse(resultado.isEmpty());
        assertEquals(2, resultado.size());
        assertEquals("Bistrô Gourmet", resultado.get(0).getNome());
        assertEquals("Pizzaria Bella", resultado.get(1).getNome());

        verify(restauranteRepository, times(1)).buscarRestaurantes("Bistrô", "São Paulo", "Francesa");
    }

    @Test
    void testAtualizarRestaurante() {
        RestauranteEntity restauranteAtualizado = new RestauranteEntity();
        restauranteAtualizado.setId(1L);
        restauranteAtualizado.setNome("Bistrô de Luxo");
        restauranteAtualizado.setLocalizacao("São Paulo");
        restauranteAtualizado.setTipoCozinha(TipoCozinhaEnum.FRANCESA);

        when(restauranteRepository.save(restauranteAtualizado)).thenReturn(restauranteAtualizado);

        RestauranteEntity updatedRestaurante = restauranteRepository.save(restauranteAtualizado);

        assertEquals("Bistrô de Luxo", updatedRestaurante.getNome());

        verify(restauranteRepository, times(1)).save(restauranteAtualizado);
    }

    @Test
    void testDeletarRestaurante() {
        doNothing().when(restauranteRepository).deleteById(1L);
        restauranteRepository.deleteById(1L);
        verify(restauranteRepository, times(1)).deleteById(1L);
    }
}
