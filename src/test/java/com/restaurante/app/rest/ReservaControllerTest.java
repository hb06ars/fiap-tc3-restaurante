package com.restaurante.app.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.domain.useCase.ReservarMesaUseCase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    @Mock
    private ReservaService reservaService;

    @Mock
    private ReservarMesaUseCase reservarMesaUseCase;

    private ObjectMapper objectMapper;  // Instanciação do ObjectMapper

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
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @AfterEach
    void tearDown() throws Exception {
        mock.close();
    }

    @Test
    void testCadastrarReserva() throws Exception {
        // Simula o comportamento do serviço
        when(reservarMesaUseCase.salvar(any(ReservaDTO.class))).thenReturn(reservaDTO);

        mockMvc.perform(post("/reserva")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))  // Usando o ObjectMapper para converter o DTO em String JSON
                .andExpect(status().isOk())  // Verifica se o status é 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Verifica o tipo de conteúdo
                .andExpect(jsonPath("$.usuarioId").value(1L))  // Verifica o usuário ID
                .andExpect(jsonPath("$.mesaId").value(1L))  // Verifica a mesa ID
                .andExpect(jsonPath("$.restauranteId").value(1L))  // Verifica o restaurante ID
                .andExpect(jsonPath("$.dataDaReserva").exists())  // Verifica se a data da reserva existe
                .andExpect(jsonPath("$.dataFimReserva").exists())  // Verifica se a data de fim da reserva existe
                .andExpect(jsonPath("$.valorReserva").value(200))  // Verifica o valor da reserva
                .andExpect(jsonPath("$.statusPagamento").value(StatusPagamentoEnum.PENDENTE.name()))  // Verifica o status de pagamento
                .andExpect(jsonPath("$.statusReserva").value(StatusReservaEnum.RESERVADO.name()));  // Verifica o status da reserva
    }

    @Test
    void testAtualizarReserva() throws Exception {
        // Simula o comportamento do serviço
        when(reservarMesaUseCase.atualizar(any(Long.class), any(ReservaDTO.class))).thenReturn(reservaDTO);

        mockMvc.perform(put("/reserva/{id}", reservaDTO.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))  // Usando o ObjectMapper para converter o DTO em String JSON
                .andExpect(status().isOk())  // Verifica se o status é 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Verifica o tipo de conteúdo
                .andExpect(jsonPath("$.usuarioId").value(1L))  // Verifica o usuário ID
                .andExpect(jsonPath("$.mesaId").value(1L))  // Verifica a mesa ID
                .andExpect(jsonPath("$.restauranteId").value(1L))  // Verifica o restaurante ID
                .andExpect(jsonPath("$.statusReserva").value(StatusReservaEnum.RESERVADO.name()));  // Verifica o status da reserva
    }

    @Test
    void testBuscarReservas() throws Exception {
        // Simula o comportamento do serviço
        when(reservaService.buscar(any(Long.class), any(ReservaDTO.class))).thenReturn(List.of(reservaDTO));

        mockMvc.perform(get("/reserva/{idrestaurante}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reservaDTO)))  // Usando o ObjectMapper para converter o DTO em String JSON
                .andExpect(status().isOk())  // Verifica se o status é 200 OK
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))  // Verifica o tipo de conteúdo
                .andExpect(jsonPath("$[0].usuarioId").value(1L))  // Verifica o usuário ID
                .andExpect(jsonPath("$[0].mesaId").value(1L))  // Verifica a mesa ID
                .andExpect(jsonPath("$[0].restauranteId").value(1L))  // Verifica o restaurante ID
                .andExpect(jsonPath("$[0].statusReserva").value(StatusReservaEnum.RESERVADO.name()));  // Verifica o status da reserva
    }
}
