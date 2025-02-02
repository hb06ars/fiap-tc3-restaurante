package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class MesaRepositoryIT {

    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void testBuscarMesasDisponiveis() {
        var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restaurante.getId());
        mesaRepository.save(mesaEntity);

        mesaRepository.buscarMesasDisponiveis(mesaEntity.getId(), LocalDateTime.now());

    }

    @Test
    void testBuscarTodasPorRestauranteId() {
        var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restaurante.getId());

        var result = mesaRepository.save(mesaEntity);
        var mesa = mesaRepository.findAllByRestauranteId(result.getRestauranteId());

        assertThat(mesa.size()).isPositive();
        assertThat(mesa.get(0).getRestauranteId()).isEqualTo(restaurante.getId());
    }

    @Test
    void testSalvarMesa() {
        var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restaurante.getId());
        MesaEntity savedMesa = mesaRepository.save(mesaEntity);

        assertNotNull(savedMesa);
        assertThat(savedMesa.getId()).isPositive();
    }

    @Test
    void testBuscarPorId() {
        var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        MesaEntity mesaAtualizada = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restaurante.getId());
        MesaEntity savedMesa = mesaRepository.save(mesaEntity);

        Optional<MesaEntity> foundMesa = mesaRepository.findById(savedMesa.getId());

        assertTrue(foundMesa.isPresent());
        assertThat(foundMesa.get().getId()).isPositive();
    }

    @Test
    void testAtualizarMesa() {
        var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        MesaEntity mesaAtualizada = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restaurante.getId());
        MesaEntity savedMesa = mesaRepository.save(mesaEntity);
        mesaAtualizada.setId(savedMesa.getId());
        mesaAtualizada.setNomeMesa("Mesa atualizada");
        mesaAtualizada = mesaRepository.save(mesaAtualizada);

        assertNotNull(mesaAtualizada);
        assertThat(mesaAtualizada.getNomeMesa()).isNotEqualTo(mesaEntity.getNomeMesa());
    }

    @Test
    void testDeletarMesa() {
        var restaurante = restauranteRepository.save(getRandom(RestauranteEntity.class));
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restaurante.getId());
        MesaEntity savedMesa = mesaRepository.save(mesaEntity);

        mesaRepository.deleteById(savedMesa.getId());
        var mesa = mesaRepository.findById(savedMesa.getId());

        assertTrue(mesa.isEmpty());
    }
}
