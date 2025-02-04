package com.restaurante.app.rest.controller;

import com.restaurante.app.rest.request.UsuarioRequest;
import com.restaurante.app.service.postgres.UsuarioService;
import com.restaurante.domain.dto.UsuarioDTO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/usuario")
@Slf4j
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar Usuário",
            description = "Criação do Usuário.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Cadastro realizado com sucesso.")
    @PostMapping("/cadastrar")
    public ResponseEntity<UsuarioDTO> cadastro(@Valid @RequestBody UsuarioRequest request) {
        log.info("requisição para cadastrar usuário foi efetuada");
        return ResponseEntity.ok(service.save(new UsuarioDTO(request)));
    }

    @Operation(summary = "Atualizar Usuário",
            description = "Atualização do Usuário.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Atualização realizada com sucesso.")
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<UsuarioDTO> atualizar(@PathVariable(name = "id") Long id,
                                                @Valid @RequestBody UsuarioRequest request) {
        log.info("requisição para atualizar usuário foi efetuada");
        return ResponseEntity.ok(service.update(id, new UsuarioDTO(request)));
    }

    @Operation(summary = "Buscar Usuário",
            description = "Buscar o Usuário.")
    @ApiResponse(responseCode = HttpStatusCodes.OK, description = "Busca realizada com sucesso.")
    @GetMapping
    public ResponseEntity<UsuarioDTO> buscar(
            @RequestParam(name = "email", required = false) String email,
            @RequestParam(name = "celular", required = false) String celular
    ) {
        log.info("requisição para buscar usuário foi efetuada");
        return ResponseEntity.ok(service.findByEmailOrCelular(email, celular));
    }

}