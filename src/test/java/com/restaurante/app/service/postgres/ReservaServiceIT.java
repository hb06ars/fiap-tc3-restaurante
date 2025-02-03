package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.infra.exceptions.FieldNotFoundException;
import com.restaurante.infra.repository.postgres.FuncionamentoRepository;
import com.restaurante.infra.repository.postgres.MesaRepository;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Transactional
class ReservaServiceIT extends BaseUnitTest {

    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private MesaRepository mesaRepository;
    @Autowired
    private FuncionamentoRepository funcionamentoRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ReservaService reservaService;

    @Test
    void save_ReturnsReservaDTO() {
        var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(4);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

        ReservaDTO dto = getRandom(ReservaDTO.class);
        dto.setMesaId(mesaSaved.getId());
        dto.setRestauranteId(restauranteSaved.getId());
        dto.setUsuarioId(usuarioSaved.getId());
        ReservaDTO result = reservaService.save(dto);

        assertNotNull(result);
        assertThat(result.getId()).isPositive();
    }

    @Test
    void findAll_ReturnsReservaDTOList() {
        var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(4);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

        ReservaDTO dto = getRandom(ReservaDTO.class);
        dto.setMesaId(mesaSaved.getId());
        dto.setRestauranteId(restauranteSaved.getId());
        dto.setUsuarioId(usuarioSaved.getId());
        reservaService.save(dto);

        List<ReservaDTO> result = reservaService.findAll();

        assertNotNull(result);
        assertThat(result.size()).isPositive();
    }

    @Test
    void findById_ReturnsReservaDTO() {
        var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(4);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

        ReservaDTO dto = getRandom(ReservaDTO.class);
        dto.setMesaId(mesaSaved.getId());
        dto.setRestauranteId(restauranteSaved.getId());
        dto.setUsuarioId(usuarioSaved.getId());
        ReservaDTO reservaSaved = reservaService.save(dto);

        ReservaDTO result = reservaService.findById(reservaSaved.getId());

        assertNotNull(result);
        assertThat(result.getId()).isPositive();

    }

    @Test
    void findById_ReturnsNull_WhenReservaNotFound() {
        ReservaDTO result = reservaService.findById(1L);
        assertNull(result);
    }

    @Test
    void update_ReturnsUpdatedReservaDTO() {
        var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(4);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

        ReservaDTO dto = getRandom(ReservaDTO.class);
        dto.setMesaId(mesaSaved.getId());
        dto.setRestauranteId(restauranteSaved.getId());
        dto.setUsuarioId(usuarioSaved.getId());
        dto.setStatusPagamento(StatusPagamentoEnum.PENDENTE);

        ReservaDTO reservaSaved = reservaService.save(dto);
        reservaSaved.setStatusPagamento(StatusPagamentoEnum.PAGO);
        ReservaDTO result = reservaService.update(reservaSaved.getId(), reservaSaved);

        assertNotNull(result);
        assertThat(result.getId()).isPositive();
    }

    @Test
    void update_ThrowsRuntimeException_WhenReservaNotFound() {
        ReservaDTO dto = getRandom(ReservaDTO.class);
        assertThrows(RuntimeException.class, () -> reservaService.update(1L, dto));
    }

    @Test
    void deleteSuccess() {
        var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(4);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);

        ReservaDTO dto = getRandom(ReservaDTO.class);
        dto.setMesaId(mesaSaved.getId());
        dto.setRestauranteId(restauranteSaved.getId());
        dto.setUsuarioId(usuarioSaved.getId());
        ReservaDTO reservaSaved = reservaService.save(dto);

        reservaService.delete(reservaSaved.getId());
        var result = reservaService.findById(reservaSaved.getId());

        assertNull(result);
    }

    @Test
    void delete_ThrowsRuntimeException_WhenReservaNotFound() {
        assertThrows(RuntimeException.class, () -> reservaService.delete(1L));
    }

    @Test
    void buscar_ThrowsFieldNotFoundException_WhenDataDaReservaIsNull() {
        ReservaDTO dto = getRandom(ReservaDTO.class);
        dto.setDataDaReserva(null);
        assertThrows(FieldNotFoundException.class, () -> reservaService.buscar(1L, dto));
    }

    @Test
    void buscar_ReturnsReservaDTOList() {
        var diaAtual = LocalDate.now().getDayOfWeek().getValue();
        var usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(4);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        MesaEntity mesaEntity = getRandom(MesaEntity.class);
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
        FuncionamentoEntity funcionamentoEntity = getRandom(FuncionamentoEntity.class);
        funcionamentoEntity.setRestauranteId(restauranteSaved.getId());
        funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaAtual));
        funcionamentoEntity.setAbertura(LocalTime.of(8, 0, 0));
        funcionamentoEntity.setFechamento(LocalTime.of(22, 0, 0));
        funcionamentoRepository.save(funcionamentoEntity);

        ReservaDTO dto = getRandom(ReservaDTO.class);
        dto.setMesaId(mesaSaved.getId());
        dto.setRestauranteId(restauranteSaved.getId());
        dto.setDataDaReserva(LocalDate.now().atTime(14, 0));
        dto.setDataFimReserva(LocalDate.now().atTime(16, 0));
        dto.setUsuarioId(usuarioSaved.getId());
        ReservaDTO reservaSaved = reservaService.save(dto);

        List<ReservaDTO> result = reservaService.buscar(reservaSaved.getRestauranteId(), reservaSaved);

        assertNotNull(result);
        assertThat(result.size()).isPositive();
    }

}
