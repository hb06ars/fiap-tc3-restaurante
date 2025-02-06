package com.restaurante.app.rest.controller;

import com.callibrity.logging.test.LogTracker;
import com.callibrity.logging.test.LogTrackerStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.domain.useCase.ReservarMesaUseCase;
import com.restaurante.infra.exceptions.GlobalExceptionHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReservaControllerTest {

    AutoCloseable mock;

    private MockMvc mockMvc;

    @RegisterExtension
    LogTrackerStub logTracker = LogTrackerStub.create().recordForLevel(LogTracker.LogLevel.INFO)
            .recordForType(ReservaController.class);

    @Mock
    private ReservaService reservaService;

    @Mock
    private ReservarMesaUseCase reservarMesaUseCase;

    private ObjectMapper objectMapper;

    private ReservaDTO reservaDTO;

    @BeforeEach
    void setUp() {
        mock = MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        reservaDTO = ReservaDTO.builder()
                .id(1L)
                .usuarioId(1L)
                .mesaId(1L)
                .restauranteId(1L)
                .dataDaReserva(LocalDateTime.now())
                .dataFimReserva(LocalDateTime.now().plusHours(2))
                .valorReserva(BigDecimal.valueOf(200L))
                .statusPagamento(StatusPagamentoEnum.PENDENTE)
                .statusReserva(StatusReservaEnum.RESERVADO)
                .build();

        ReservaController controller = new ReservaController(reservarMesaUseCase, reservaService);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Nested
    class CadastrarReserva {
        @Test
        void testCadastrarReserva() throws Exception {
            when(reservarMesaUseCase.salvar(any(ReservaDTO.class))).thenReturn(reservaDTO);

            mockMvc.perform(post("/reserva")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reservaDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.usuarioId").value(1L))
                    .andExpect(jsonPath("$.mesaId").value(1L))
                    .andExpect(jsonPath("$.restauranteId").value(1L))
                    .andExpect(jsonPath("$.dataDaReserva").exists())
                    .andExpect(jsonPath("$.dataFimReserva").exists())
                    .andExpect(jsonPath("$.valorReserva").value(200))
                    .andExpect(jsonPath("$.statusPagamento").value(StatusPagamentoEnum.PENDENTE.name()))
                    .andExpect(jsonPath("$.statusReserva").value(StatusReservaEnum.RESERVADO.name()));
        }
    }

    @Nested
    class AtualizarReserva {
        @Test
        void testAtualizarReserva() throws Exception {

            when(reservarMesaUseCase.atualizar(any(Long.class), any(ReservaDTO.class))).thenReturn(reservaDTO);

            mockMvc.perform(put("/reserva/{id}", reservaDTO.getId())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reservaDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.usuarioId").value(1L))
                    .andExpect(jsonPath("$.mesaId").value(1L))
                    .andExpect(jsonPath("$.restauranteId").value(1L))
                    .andExpect(jsonPath("$.statusReserva").value(StatusReservaEnum.RESERVADO.name()));
        }
    }

    @Nested
    class BuscarReserva {
        @Test
        void testBuscarReservas() throws Exception {

            when(reservaService.buscar(any(Long.class), any(ReservaDTO.class))).thenReturn(List.of(reservaDTO));

            mockMvc.perform(get("/reserva/{idrestaurante}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(reservaDTO)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$[0].usuarioId").value(1L))
                    .andExpect(jsonPath("$[0].mesaId").value(1L))
                    .andExpect(jsonPath("$[0].restauranteId").value(1L))
                    .andExpect(jsonPath("$[0].statusReserva").value(StatusReservaEnum.RESERVADO.name()));
        }
    }

}
