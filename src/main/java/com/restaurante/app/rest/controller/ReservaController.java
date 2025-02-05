package com.restaurante.app.rest.controller;

import com.restaurante.app.rest.request.ReservaFilter;
import com.restaurante.app.rest.request.ReservaRequest;
import com.restaurante.app.service.postgres.ReservaService;
import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.useCase.ReservarMesaUseCase;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reserva")
@Slf4j
public class ReservaController {

    private final ReservarMesaUseCase reservarMesaUseCase;
    private final ReservaService reservaService;

    public ReservaController(ReservarMesaUseCase reservarMesaUseCase, ReservaService reservaService) {
        this.reservarMesaUseCase = reservarMesaUseCase;
        this.reservaService = reservaService;
    }

    @Operation(summary = "Cadastrar Reserva",
            description = "Salvar a Reserva que o cliente fizer.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping
    public ResponseEntity<ReservaDTO> cadastro(@Valid @RequestBody ReservaRequest request) {
        log.info("requisição para salvar reserva foi efetuada");
        return ResponseEntity.ok(reservarMesaUseCase.salvar(new ReservaDTO(request)));
    }

    @Operation(summary = "Atualizar Reserva",
            description = "Atualizar a Reserva do cliente, exemplo: Concluir, Cancelar, Alterar a reserva.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualizção realizada com sucesso.")
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDTO> atualizacao(@PathVariable(name = "id") Long id,
                                                  @Valid @RequestBody ReservaRequest request) {
        log.info("requisição para atualizar reserva foi efetuada");
        return ResponseEntity.ok(reservarMesaUseCase.atualizar(id, new ReservaDTO(request)));
    }

    @Operation(summary = "Buscar Reservas",
            description = "Buscar as Reservas.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Busca realizada com sucesso.")
    @GetMapping("/{idrestaurante}")
    public ResponseEntity<List<ReservaDTO>> buscar(@PathVariable(name = "idrestaurante") Long idrestaurante,
                                                   @Valid @RequestBody ReservaFilter request) {
        log.info("requisição para buscar reserva pelo idRestaurante foi efetuada");
        return ResponseEntity.ok(reservaService.buscar(idrestaurante, new ReservaDTO(request)));
    }


}