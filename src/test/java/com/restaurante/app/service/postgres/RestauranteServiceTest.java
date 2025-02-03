package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.TipoCozinhaEnum;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RestauranteServiceTest {

    AutoCloseable openMocks;

    @Mock
    private RestauranteRepository restauranteRepository;

    @InjectMocks
    private RestauranteService restauranteService;

    private RestauranteDTO restauranteDTO;
    private RestauranteEntity restauranteEntity;

    @BeforeEach
    void setUp() {
        openMocks = MockitoAnnotations.openMocks(this);
        restauranteDTO = new RestauranteDTO(1L, "Restaurante A", "Local A", TipoCozinhaEnum.BRASILEIRA, 50);
        restauranteEntity = new RestauranteEntity(restauranteDTO);
    }

    @AfterEach
    void tearDown() throws Exception {
        openMocks.close();
    }

    @Test
    void save_ReturnsRestauranteDTO() {
        when(restauranteRepository.save(any(RestauranteEntity.class))).thenReturn(restauranteEntity);

        RestauranteDTO result = restauranteService.save(restauranteDTO);

        assertNotNull(result);
        assertEquals(restauranteDTO.getNome(), result.getNome());
        assertEquals(restauranteDTO.getLocalizacao(), result.getLocalizacao());
    }

    @Test
    void findAll_ReturnsRestauranteDTOList() {
        when(restauranteRepository.findAll()).thenReturn(List.of(restauranteEntity));

        List<RestauranteDTO> result = restauranteService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(restauranteDTO.getNome(), result.get(0).getNome());
    }

    @Test
    void findById_ReturnsRestauranteDTO() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restauranteEntity));

        RestauranteDTO result = restauranteService.findById(1L);

        assertNotNull(result);
        assertEquals(restauranteDTO.getNome(), result.getNome());
    }

    @Test
    void findById_ReturnsNull_WhenRestauranteNotFound() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        RestauranteDTO result = restauranteService.findById(1L);

        assertNull(result);
    }

    @Test
    void update_ReturnsUpdatedRestauranteDTO() {
        when(restauranteRepository.save(any(RestauranteEntity.class))).thenReturn(restauranteEntity);
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restauranteEntity));

        RestauranteDTO updatedDTO = new RestauranteDTO(1L, "Restaurante A", "Local A", TipoCozinhaEnum.BRASILEIRA, 60);
        RestauranteDTO result = restauranteService.update(1L, updatedDTO);

        assertNotNull(result);
        assertEquals(updatedDTO.getNome(), result.getNome());
        assertEquals(updatedDTO.getLocalizacao(), result.getLocalizacao());
        assertEquals(updatedDTO.getTipoCozinha(), result.getTipoCozinha());
    }

    @Test
    void update_ThrowsObjectNotFoundException_WhenRestauranteNotFound() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> restauranteService.update(1L, restauranteDTO));

        assertEquals("Restaurante não encontrado no sistema!", thrown.getMessage());
    }

    @Test
    void deleteSuccess() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.of(restauranteEntity));

        restauranteService.delete(1L);

        verify(restauranteRepository, times(1)).findById(1L);
    }

    @Test
    void delete_ThrowsRuntimeException_WhenRestauranteNotFound() {
        when(restauranteRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> restauranteService.delete(1L));

        assertEquals("Restaurante com ID: 1, não encontrado.", thrown.getMessage());
    }

    @Test
    void buscarRestaurantes_ReturnsRestauranteDTOList() {
        when(restauranteRepository.buscarRestaurantes(anyString(), anyString(), anyString()))
                .thenReturn(List.of(restauranteEntity));

        List<RestauranteDTO> result = restauranteService.buscarRestaurantes("Restaurante A", "Local A", "Comida Brasileira");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void restauranteJaExiste_ReturnsTrue_WhenRestaurantExists() {
        Mockito.when(restauranteRepository.buscarRestaurantes("Restaurante Teste", "Localização Teste", null))
                .thenReturn(List.of(restauranteEntity));

        boolean resultado = restauranteService.restauranteJaExiste("Restaurante Teste", "Localização Teste");
        assertTrue(resultado);
    }

    @Test
    void restauranteJaExiste_ReturnsFalse_WhenRestaurantDoesNotExist() {
        Mockito.when(restauranteRepository.buscarRestaurantes("Restaurante Teste", "Localização Teste", null))
                .thenReturn(List.of());

        boolean resultado = restauranteService.restauranteJaExiste("Restaurante Teste", "Localização Teste");
        assertFalse(resultado);
    }

}
