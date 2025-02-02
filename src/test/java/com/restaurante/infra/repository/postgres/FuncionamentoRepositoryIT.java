package com.restaurante.infra.repository.postgres;


import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.enums.DiaEnum;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.restaurante.utils.TestUtil.getRandom;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class repositoryIT {

    @Autowired
    private FuncionamentoRepository repository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    private FuncionamentoEntity funcionamento;

    @Test
    void devePermitirCriarTabela() {
        long totalTabelasCriada = repository.count();
        assertThat(totalTabelasCriada).isNotNegative();
    }


    @Test
    void testValidarData_whenDataIsValid() {
        var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
        var funcionamentoEntity = getRandom(FuncionamentoEntity.class);
        funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(LocalDate.now().getDayOfWeek().getValue()));
        funcionamentoEntity.setRestauranteId(restauranteEntity.getId());
        funcionamentoEntity.setAbertura(LocalTime.of(8, 0));
        funcionamentoEntity.setFechamento(LocalTime.of(18, 0));
        var funcionamentoSaved = repository.save(funcionamentoEntity);

        var dataReserva = LocalDateTime.of(LocalDate.now(), funcionamentoSaved.getAbertura().plusHours(1));

        var dataValidada = repository.validarData(funcionamentoSaved.getId(), dataReserva, funcionamentoSaved.getDiaEnum().name());
        assertFalse(dataValidada.isEmpty());
    }

    @Test
    void testValidarData_whenDataIsInvalid() {
        var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));
        var funcionamentoEntity = getRandom(FuncionamentoEntity.class);
        funcionamentoEntity.setRestauranteId(restauranteEntity.getId());
        funcionamentoEntity.setDiaEnum(DiaEnum.fromInt(LocalDate.now().getDayOfWeek().getValue()));
        funcionamentoEntity.setAbertura(LocalTime.of(8, 0));
        funcionamentoEntity.setFechamento(LocalTime.of(18, 0));
        var funcionamentoSaved = repository.save(funcionamentoEntity);

        var dataReserva = LocalDateTime.of(LocalDate.now(), funcionamentoSaved.getFechamento().plusHours(1));

        var dataValidada = repository.validarData(funcionamentoSaved.getId(), dataReserva, funcionamentoSaved.getDiaEnum().name());
        assertTrue(dataValidada.isEmpty());
    }

    @Test
    void testBuscarTodosFUncionamentosPorRestauranteId() {
        var restauranteEntity = restauranteRepository.save(getRandom(RestauranteEntity.class));

        var diaAtual = LocalDate.now().getDayOfWeek().getValue();
        FuncionamentoEntity funcionamento1 = FuncionamentoEntity.builder()
                .id(1L)
                .diaEnum(DiaEnum.fromInt(LocalDate.now().getDayOfWeek().getValue()))
                .abertura(LocalTime.of(8, 0))
                .fechamento(LocalTime.of(22, 0))
                .restauranteId(restauranteEntity.getId())
                .build();

        var diaSeguinte = LocalDate.now().plusDays(1).getDayOfWeek().getValue();
        FuncionamentoEntity funcionamento2 = FuncionamentoEntity.builder()
                .id(2L)
                .diaEnum(DiaEnum.fromInt(diaSeguinte))
                .abertura(LocalTime.of(8, 0))
                .fechamento(LocalTime.of(22, 0))
                .restauranteId(restauranteEntity.getId())
                .build();

        var funcionamentoSaved = repository.saveAll(List.of(funcionamento1, funcionamento2));
       var resultado = repository.findAllByRestauranteId(funcionamentoSaved.get(0).getRestauranteId());

        assertEquals(2, resultado.size());
        assertEquals(DiaEnum.fromInt(diaAtual), resultado.get(0).getDiaEnum());
        assertEquals(DiaEnum.fromInt(diaSeguinte), resultado.get(1).getDiaEnum());

    }


    @Test
    void testSalvarFuncionamento() {
        when(repository.save(funcionamento)).thenReturn(funcionamento);

        FuncionamentoEntity savedFuncionamento = repository.save(funcionamento);

        assertNotNull(savedFuncionamento);
        assertEquals(1L, savedFuncionamento.getId());
        assertEquals(1L, savedFuncionamento.getRestauranteId());
        assertEquals(DiaEnum.SEGUNDA, savedFuncionamento.getDiaEnum());

        verify(repository, times(1)).save(funcionamento);
    }

    @Test
    void testBuscarPorId() {
        when(repository.findById(1L)).thenReturn(Optional.of(funcionamento));

        Optional<FuncionamentoEntity> foundFuncionamento = repository.findById(1L);

        assertTrue(foundFuncionamento.isPresent());
        assertEquals(1L, foundFuncionamento.get().getId());

        verify(repository, times(1)).findById(1L);
    }


    @Test
    void testAtualizarFuncionamento() {
        FuncionamentoEntity funcionamentoAtualizado = new FuncionamentoEntity();
        funcionamentoAtualizado.setId(1L);
        funcionamentoAtualizado.setRestauranteId(1L);
        funcionamentoAtualizado.setDiaEnum(DiaEnum.SEGUNDA);
        funcionamentoAtualizado.setAbertura(LocalTime.of(9, 0));
        funcionamentoAtualizado.setFechamento(LocalTime.of(21, 0));

        when(repository.save(funcionamentoAtualizado)).thenReturn(funcionamentoAtualizado);
        FuncionamentoEntity updatedFuncionamento = repository.save(funcionamentoAtualizado);

        assertEquals(LocalTime.of(9, 0), updatedFuncionamento.getAbertura());
        assertEquals(LocalTime.of(21, 0), updatedFuncionamento.getFechamento());

        verify(repository, times(1)).save(funcionamentoAtualizado);
    }

    @Test
    void testDeletarFuncionamento() {
        doNothing().when(repository).deleteById(1L);
        repository.deleteById(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}
