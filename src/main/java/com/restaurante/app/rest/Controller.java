package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.AvaliacaoService;
import com.restaurante.app.service.postgres.FuncionamentoService;
import com.restaurante.app.service.postgres.MesaService;
import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.app.service.postgres.RestauranteService;
import com.restaurante.app.service.postgres.UsuarioService;
import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.AvaliacaoEntity;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.domain.enums.StatusPagamentoEnum;
import com.restaurante.domain.enums.StatusReservaEnum;
import com.restaurante.domain.enums.TipoCozinhaEnum;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/restaurante")
@Slf4j
public class Controller {

    @Autowired
    RestauranteService restauranteService;

    @Autowired
    MesaService mesaService;

    @Autowired
    ReservaService reservaService;

    @Autowired
    FuncionamentoService funcionamentoService;

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    AvaliacaoService avaliacaoService;

    @Operation(summary = "Cadastrar Restaurante",
            description = "Cadastro realizado pelos restaurantes.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping("/cadastrar")
    public ResponseEntity<RestauranteDTO> cadastro() {

        var restaurante = restauranteService.save(RestauranteEntity.builder()
                .nome("La Brasa")
                .tipoCozinha(TipoCozinhaEnum.BRASILEIRA)
                .localizacao("Av. Paulista 1000")
                .capacidade(100)
                .build());

        funcionamentoService.save(FuncionamentoEntity.builder()
                .abertura(LocalTime.now())
                .fechamento(LocalTime.now().minusHours(5))
                .diaEnum(DiaEnum.SEGUNDA)
                .restauranteId(restauranteService.findAll().get(0).getId())
                .build());

        mesaService.save(MesaEntity.builder()
                .restauranteId(restauranteService.findAll().get(0).getId())
                .nomeMesa("MESA 1")
                .build());

        usuarioService.save(UsuarioEntity.builder()
                .celular("(11) 9 8855-4411")
                .email("hbasd@mail.com")
                .nome("Henrique")
                .build());

        avaliacaoService.save(AvaliacaoEntity.builder()
                .restauranteId(restauranteService.findAll().get(0).getId())
                .comentario("Muito bom! Gostei do atendimento.")
                .nota(10)
                .usuarioId(usuarioService.findAll().get(0).getId())
                .build());

        reservaService.save(ReservaEntity.builder()
                .dataDaReserva(LocalDateTime.now())
                .restauranteId(restauranteService.findAll().get(0).getId())
                .mesaId(mesaService.findAll().get(0).getId())
                .statusPagamento(StatusPagamentoEnum.PENDENTE)
                .statusReserva(StatusReservaEnum.RESERVADO)
                .valorReserva(BigDecimal.valueOf(100))
                .usuarioId(usuarioService.findAll().get(0).getId())
                .build());

        return ResponseEntity.ok(restaurante);

    }

}