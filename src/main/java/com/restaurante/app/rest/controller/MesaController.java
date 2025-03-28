package com.restaurante.app.rest.controller;

import com.restaurante.app.rest.request.MesaRequest;
import com.restaurante.app.service.postgres.MesaService;
import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mesa")
@Slf4j
public class MesaController {

    private final MesaService service;

    public MesaController(MesaService service) {
        this.service = service;
    }

    @Operation(summary = "Atualizar Mesa",
            description = "Atualizar a Mesa do Restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<MesaDTO> atualizar(@PathVariable(name = "id") Long id,
                                             @Valid @RequestBody MesaRequest request) {
        log.info("requisição para atualizar mesa foi efetuada");
        return ResponseEntity.ok(service.update(id, new MesaDTO(request)));
    }

    @Operation(summary = "Buscar Mesas",
            description = "Buscar a Mesa.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Busca realizada com sucesso.")
    @GetMapping(("/{id}"))
    public ResponseEntity<List<MesaDisponivelDTO>> buscarMesasDisponiveisPeloRestaurante(
            @PathVariable(name = "id") Long id) {
        log.info("requisição para buscar mesa foi efetuada");
        return ResponseEntity.ok(service.buscarMesas(id));
    }

    @Operation(summary = "Buscar Mesas por Restaurante",
            description = "Buscar a Mesas por Restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Busca realizada com sucesso.")
    @GetMapping(("/listaporrestaurante/{idRestaurante}"))
    public ResponseEntity<List<MesaDTO>> buscarMesasPorRestaurante(
            @PathVariable(name = "idRestaurante") Long idRestaurante) {
        log.info("requisição para buscar mesa pelo idRestaurante foi efetuada");
        return ResponseEntity.ok(service.findAllByIdRestaurante(idRestaurante));
    }

}