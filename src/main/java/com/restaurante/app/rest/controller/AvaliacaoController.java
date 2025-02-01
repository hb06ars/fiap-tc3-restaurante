package com.restaurante.app.rest.controller;

import com.restaurante.app.service.postgres.AvaliacaoService;
import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/avaliacao")
@Slf4j
public class AvaliacaoController {

    private final AvaliacaoService service;

    public AvaliacaoController(AvaliacaoService service) {
        this.service = service;
    }

    @Operation(summary = "Registrar Avaliação",
            description = "Salvar a avaliação.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Registro salvo com sucesso.")
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> avaliar(@RequestBody AvaliacaoDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @Operation(summary = "Buscar Avaliação Restaurante",
            description = "Lista todas as avaliações pelo Restaurante.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Registros encontrados com sucesso.")
    @GetMapping("/{idRestaurante}")
    public ResponseEntity<List<AvaliacaoDTO>> buscar(@PathVariable(name = "idRestaurante") Long idRestaurante) {
        return ResponseEntity.ok(service.listarPorRestaurante(idRestaurante));
    }

}