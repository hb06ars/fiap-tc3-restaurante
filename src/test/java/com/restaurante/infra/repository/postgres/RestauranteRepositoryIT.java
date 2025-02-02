package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.RestauranteEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class RestauranteRepositoryIT {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void testBuscarRestaurantesPorFiltro() {
        RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));

        List<RestauranteEntity> resultado = restauranteRepository.buscarRestaurantes(
                restauranteSaved.getNome(),
                restauranteSaved.getLocalizacao(),
                restauranteSaved.getTipoCozinha().name()
        );

        assertNotNull(resultado);
        assertThat(resultado.size()).isPositive();
    }

    @Test
    void testSalvarRestaurante() {
        RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
        assertNotNull(restauranteSaved);
        assertThat(restauranteSaved.getId()).isPositive();
    }

    @Test
    void testBuscarPorId() {
        RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
        var result = restauranteRepository.findById(restauranteSaved.getId());
        assertTrue(result.isPresent());
        assertThat(result.get().getId()).isPositive();
    }

    @Test
    void testAtualizarRestaurante() {
        var entity = getRandom(RestauranteEntity.class);
        entity.setNome("Primeiro nome");
        RestauranteEntity restauranteSaved = restauranteRepository.save(entity);
        var restauranteAtualizar = restauranteRepository.findById(restauranteSaved.getId());
        restauranteAtualizar.get().setNome("Atualizar nome");
        var result = restauranteRepository.save(restauranteAtualizar.get());

        assertNotNull(result);
        assertThat(result.getId()).isEqualTo(restauranteSaved.getId());
        assertThat(result.getNome()).isNotEqualTo(entity.getNome());
    }

    @Test
    void testDeletarRestaurante() {
        RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));

        restauranteRepository.deleteById(restauranteSaved.getId());

        var restauranteAtualizar = restauranteRepository.findById(restauranteSaved.getId());
        assertFalse(restauranteAtualizar.isPresent());
    }
}
