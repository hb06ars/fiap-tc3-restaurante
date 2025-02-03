package com.restaurante.domain.useCase.impl;

import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.exceptions.ReservaException;
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
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ValidaDataUseCaseImplIT extends BaseUnitTest {

    @Autowired
    private ValidarDataUseCaseImpl validaDataUseCase;
    @Autowired
    private FuncionamentoRepository funcionamentoRepository;
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private MesaRepository mesaRepository;

    @Test
    void testExecuteSuccess() {
        LocalDateTime dataReserva = LocalDate.now().atTime(10, 0);
        LocalDate diaSelecionado = LocalDate.now();
        usuarioRepository.save(getRandom(UsuarioEntity.class));
        var restauranteEntity = getRandom(RestauranteEntity.class);
        restauranteEntity.setCapacidade(10);
        var restauranteSaved = restauranteRepository.save(restauranteEntity);
        var mesaEntity = getRandom(MesaEntity.class);
        var diaAtual = LocalDate.now().getDayOfWeek().getValue();
        mesaEntity.setRestauranteId(restauranteSaved.getId());
        mesaRepository.save(mesaEntity);
        FuncionamentoEntity funcionamentoEntity = getRandom(FuncionamentoEntity.class);
        funcionamentoEntity.setRestauranteId(restauranteSaved.getId());
        funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(diaAtual));
        funcionamentoEntity.setAbertura(LocalTime.of(8, 0, 0));
        funcionamentoEntity.setFechamento(LocalTime.of(22, 0, 0));
        funcionamentoRepository.save(funcionamentoEntity);

        validaDataUseCase.execute(1L, dataReserva, diaSelecionado);
    }

    @Test
    void testExecute_DataInvalida() {
        LocalDateTime dataReserva = LocalDate.now().atTime(9, 0);
        LocalDate diaSelecionado = LocalDate.now();
        assertThrows(ReservaException.class, () -> validaDataUseCase.execute(1L, dataReserva, diaSelecionado));
    }

    @Test
    void testBuscaDiaDaSemana_Feriado() {
        LocalDate diaSelecionado = LocalDate.of(2025, 12, 25);
        DiaEnum diaEnum = validaDataUseCase.buscaDiaDaSemana(diaSelecionado);
        assertEquals(DiaEnum.FERIADOS, diaEnum);
    }

    @Test
    void testBuscaDiaDaSemana_Semana() {
        LocalDate diaSelecionado = LocalDate.of(2025, 1, 27);
        DiaEnum diaEnum = validaDataUseCase.buscaDiaDaSemana(diaSelecionado);
        assertEquals(DiaEnum.SEGUNDA, diaEnum);
    }
}
