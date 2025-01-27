package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.AvaliacaoService;
import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.domain.entity.AvaliacaoEntity;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/avaliacao")
@Slf4j
public class AvaliacaoController {

    @Autowired
    AvaliacaoService service;

    @Operation(summary = "Registrar Avaliação",
            description = "Salvar a avaliação.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Registro salvo com sucesso.")
    @PostMapping
    public ResponseEntity<AvaliacaoDTO> avaliar(@Valid @RequestBody AvaliacaoEntity entity) {
        return ResponseEntity.ok(service.save(entity));
    }

}