package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.infra.exceptions.ReservaException;
import com.restaurante.infra.repository.postgres.MesaRepository;
import com.restaurante.infra.repository.postgres.ReservaRepository;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class BuscarMesaDisponivelUseCaseImplIT extends BaseUnitTest {

    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private BuscarMesaDisponivelUseCaseImpl buscarMesaUseCase;

    @Test
    void executeDeveRetornarMesaDisponivelDTO() {
        var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
        var mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        var mesaSaved = mesaRepository.save(mesaEntity);
        var reservaEntity = getRandom(ReservaEntity.class);
        reservaEntity.setMesaId(mesaSaved.getId());
        reservaEntity.setRestauranteId(restauranteSaved.getId());
        reservaEntity.setUsuarioId(usuarioSaved.getId());
        reservaRepository.save(reservaEntity);

        MesaDisponivelDTO resultado = buscarMesaUseCase.execute(restauranteSaved.getId(), LocalDateTime.now());
        assertNotNull(resultado);
        assertThat(resultado.getMesaId()).isPositive();
    }

    @Test
    void executeDeveLancarReservaExceptionQuandoNaoHouverMesasDisponiveis() {
        assertThrows(ReservaException.class, () -> buscarMesaUseCase.execute(1L, LocalDateTime.now()));
    }
}
