package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class RestauranteServiceIT  extends BaseUnitTest {

    @Autowired
    private RestauranteRepository restauranteRepository;

    @Autowired
    private RestauranteService restauranteService;

    private RestauranteDTO restauranteDTO;
    private RestauranteEntity restauranteEntity;

    @Test
    void save_ReturnsRestauranteDTO() {
        RestauranteDTO result = restauranteService.save(restauranteDTO);
    }

    @Test
    void findAll_ReturnsRestauranteDTOList() {
        List<RestauranteDTO> result = restauranteService.findAll();
    }

    @Test
    void findById_ReturnsRestauranteDTO() {
        RestauranteDTO result = restauranteService.findById(1L);
    }

    @Test
    void findById_ReturnsNull_WhenRestauranteNotFound() {
        RestauranteDTO result = restauranteService.findById(1L);
    }

    @Test
    void update_ReturnsUpdatedRestauranteDTO() {
        RestauranteDTO result = restauranteService.update(1L, null);
    }

    @Test
    void update_ThrowsObjectNotFoundException_WhenRestauranteNotFound() {
        ObjectNotFoundException thrown = assertThrows(ObjectNotFoundException.class, () -> restauranteService.update(1L, restauranteDTO));
    }

    @Test
    void delete_Success() {
        restauranteService.delete(1L);
    }

    @Test
    void delete_ThrowsRuntimeException_WhenRestauranteNotFound() {
        RuntimeException thrown = assertThrows(RuntimeException.class, () -> restauranteService.delete(1L));
    }

    @Test
    void buscarRestaurantes_ReturnsRestauranteDTOList() {
        List<RestauranteDTO> result = restauranteService.buscarRestaurantes("Restaurante A", "Local A", "Comida Brasileira");
    }

    @Test
    void restauranteJaExiste_ReturnsTrue_WhenRestaurantExists() {
        boolean resultado = restauranteService.restauranteJaExiste("Restaurante Teste", "Localização Teste");
    }

    @Test
    void restauranteJaExiste_ReturnsFalse_WhenRestaurantDoesNotExist() {
        boolean resultado = restauranteService.restauranteJaExiste("Restaurante Teste", "Localização Teste");
    }

}
