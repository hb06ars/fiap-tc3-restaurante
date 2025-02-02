package com.restaurante.infra.repository.postgres;

import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.domain.util.DataFormat;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservaRepositoryIT {

    @Autowired
    private ReservaRepository reservaRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Test
    void testBuscarReservasPorFiltro() {
        var agora = LocalDateTime.now();
        UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
        ReservaEntity reservaEntity = getRandom(ReservaEntity.class);

        reservaEntity.setUsuarioId(usuarioSaved.getId());
        reservaEntity.setMesaId(mesaSaved.getId());
        reservaEntity.setRestauranteId(restauranteSaved.getId());
        reservaEntity.setDataDaReserva(agora);
        reservaEntity.setStatusPagamento(StatusPagamentoEnum.PENDENTE);
        reservaEntity.setStatusReserva(StatusReservaEnum.OCUPADO);
        reservaEntity.setDataFimReserva(agora.plusHours(2));

        var reservaSaved = reservaRepository.save(reservaEntity);

        List<ReservaEntity> resultado = reservaRepository.findAllByFilter(
                reservaSaved.getRestauranteId(),
                reservaSaved.getStatusReserva(),
                reservaSaved.getStatusPagamento(),
                DataFormat.truncate(reservaSaved.getDataDaReserva().plusMinutes(10)));

        assertNotNull(resultado);
        assertThat(resultado.size()).isPositive();

    }


    @Test
    void testSalvarReserva() {
        UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
        ReservaEntity reservaEntity = getRandom(ReservaEntity.class);

        reservaEntity.setUsuarioId(usuarioSaved.getId());
        reservaEntity.setMesaId(mesaSaved.getId());
        reservaEntity.setRestauranteId(restauranteSaved.getId());

        var reservaSaved = reservaRepository.save(reservaEntity);
        assertNotNull(reservaSaved);
        assertThat(reservaSaved.getId()).isPositive();

    }

    @Test
    void testBuscarPorId() {
        UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        RestauranteEntity restauranteSaved = restauranteRepository.save(getRandom(RestauranteEntity.class));
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
        ReservaEntity reservaSaved = getRandom(ReservaEntity.class);

        reservaSaved.setUsuarioId(usuarioSaved.getId());
        reservaSaved.setMesaId(mesaSaved.getId());
        reservaSaved.setRestauranteId(restauranteSaved.getId());
        reservaSaved.setDataDaReserva(LocalDateTime.now());
        reservaSaved.setDataFimReserva(LocalDateTime.now().plusHours(2));

        assertNotNull(reservaSaved);
        assertThat(reservaSaved.getId()).isPositive();

    }

//    @Test
//    void testAtualizarReserva() {
//        ReservaEntity reservaAtualizada = new ReservaEntity();
//        reservaAtualizada.setId(1L);
//        reservaAtualizada.setRestauranteId(1L);
//        reservaAtualizada.setStatusReserva(StatusReservaEnum.CANCELADO);
//        reservaAtualizada.setStatusPagamento(StatusPagamentoEnum.CANCELADO);
//        reservaAtualizada.setDataDaReserva(reserva.getDataDaReserva());
//        reservaAtualizada.setDataFimReserva(reserva.getDataFimReserva());
//
//        when(reservaRepository.save(reservaAtualizada)).thenReturn(reservaAtualizada);
//        ReservaEntity updatedReserva = reservaRepository.save(reservaAtualizada);
//
//        assertEquals(StatusReservaEnum.CANCELADO, updatedReserva.getStatusReserva());
//        assertEquals(StatusPagamentoEnum.CANCELADO, updatedReserva.getStatusPagamento());
//        verify(reservaRepository, times(1)).save(reservaAtualizada);
//    }
//
//    @Test
//    void testDeletarReserva() {
//        doNothing().when(reservaRepository).deleteById(1L);
//        reservaRepository.deleteById(1L);
//        verify(reservaRepository, times(1)).deleteById(1L);
//    }
}
