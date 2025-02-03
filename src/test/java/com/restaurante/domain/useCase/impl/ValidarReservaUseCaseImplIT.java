package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.repository.postgres.MesaRepository;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
import com.restaurante.utils.BaseUnitTest;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class ValidarReservaUseCaseImplIT extends BaseUnitTest {

    @Autowired
    private ValidarReservaUseCaseImpl validarReservaUseCase;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MesaRepository mesaRepository;

    @Test
    void testExecuteSuccess() {
        UsuarioEntity usuarioSaved = usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(10);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        var mesaEntity = getRandom(MesaEntity.class);
        var diaAtual = LocalDate.now().getDayOfWeek().getValue();
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        MesaEntity mesaSaved = mesaRepository.save(mesaEntity);
        FuncionamentoEntity funcionamentoEntity = getRandom(FuncionamentoEntity.class);
        funcionamentoEntity.setRestauranteId(restauranteSaved.getId());
        funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaAtual));
        funcionamentoEntity.setAbertura(LocalTime.of(8, 0, 0));
        funcionamentoEntity.setFechamento(LocalTime.of(22, 0, 0));

        ReservaDTO dto = getRandom(ReservaDTO.class);
        dto.setRestauranteId(restauranteSaved.getId());
        dto.setMesaId(mesaSaved.getId());
        dto.setUsuarioId(usuarioSaved.getId());
        dto.setDataDaReserva(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 10, 0, 0));
        dto.setDataFimReserva(LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 12, 0, 0));

        validarReservaUseCase.execute(dto);
    }
}
