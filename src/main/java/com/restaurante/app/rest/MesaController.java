package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.MesaService;
import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/mesa")
@Slf4j
public class MesaController {

    @Autowired
    MesaService service;

    @Operation(summary = "Cadastrar Mesa",
            description = "Salvar a Mesa.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping("/cadastrar")
    public ResponseEntity<MesaDTO> cadastro(@Valid @RequestBody MesaEntity entity) {
        return ResponseEntity.ok(service.save(entity));
    }

    @Operation(summary = "Buscar Mesas",
            description = "Buscar a Mesa.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Busca realizada com sucesso.")
    @GetMapping(("/{id}"))
    public ResponseEntity<List<MesaDisponivelDTO>> buscarMesas(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarMesas(id));
    }

}