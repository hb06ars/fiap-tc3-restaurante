package com.restaurante.app.rest;

import com.restaurante.app.service.postgres.UsuarioService;
import com.restaurante.domain.dto.UsuarioDTO;
import com.restaurante.domain.util.HttpStatusCodes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/usuario")
@Slf4j
public class UsuarioController {

    @Autowired
    UsuarioService service;

    @Operation(summary = "Cadastrar Usuário",
            description = "Criação do Usuário.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDTO> cadastro(@Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.save(dto));
    }

    @Operation(summary = "Atualizar Usuário",
            description = "Atualização do Usuário.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualização realizada com sucesso.")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable(name = "id") Long id,
                                                @Valid @RequestBody UsuarioDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

}