package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.UsuarioDTO;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.domain.util.AjustesString;
import com.restaurante.infra.exceptions.RecordAlreadyExistsException;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    @Autowired
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UsuarioDTO save(UsuarioDTO dto) {
        UsuarioEntity usuarioExiste = repository.findByEmailOrCelular(dto.getEmail(), AjustesString.removerCaracteresCel(dto.getCelular()));
        if (usuarioExiste == null)
            return new UsuarioDTO(repository.save(new UsuarioEntity(dto)));
        throw new RecordAlreadyExistsException("O email ou celular já existem no sistema.");
    }

    @Transactional(readOnly = true)
    public List<UsuarioDTO> findAll() {
        return repository.findAll().stream().map(UsuarioDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public UsuarioDTO findById(Long id) {
        Optional<UsuarioEntity> obj = repository.findById(id);
        return obj.map(UsuarioDTO::new).orElse(null);
    }

    @Transactional
    public UsuarioDTO update(Long id, UsuarioDTO usuarioSalvar) {
        Optional<UsuarioEntity> usuarioExistente = repository.findById(id);
        if (usuarioExistente.isPresent()) {
            return new UsuarioDTO(repository.save(usuarioExistente.get()));
        } else {
            throw new RuntimeException("Usuário " + id + " não encontrado.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.findById(id);
        } else {
            throw new RuntimeException("Usuário com ID: " + id + ", não encontrado.");
        }
    }

}

