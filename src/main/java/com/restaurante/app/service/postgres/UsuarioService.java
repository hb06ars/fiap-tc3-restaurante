package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.UsuarioDTO;
import com.restaurante.domain.entity.UsuarioEntity;
import com.restaurante.infra.repository.postgres.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
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
    public UsuarioDTO save(UsuarioEntity usuarioEntity) {
        try {
            return new UsuarioDTO(repository.save(usuarioEntity));
        } catch (OptimisticLockingFailureException e) {
            var usuarioExistente = repository.findById(usuarioEntity.getId()).orElse(null);
            if (usuarioExistente != null) {
                return new UsuarioDTO(repository.save(usuarioExistente));
            } else {
                throw new RuntimeException("Houve um problema para a usuario, tente novamente!");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<UsuarioEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public UsuarioEntity findById(Long id) {
        Optional<UsuarioEntity> obj = repository.findById(id);
        return obj.orElse(null);
    }

    @Transactional
    public UsuarioEntity update(Long id, UsuarioEntity usuarioSalvar) {
        try {
            Optional<UsuarioEntity> usuarioExistente = repository.findById(id);
            if (usuarioExistente.isPresent()) {
                return repository.save(usuarioExistente.get());
            } else {
                throw new RuntimeException("Usuario " + id + " não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var usuarioExistente = repository.findById(usuarioSalvar.getId()).orElse(null);
            if (usuarioExistente != null) {
                return repository.save(usuarioExistente);
            } else {
                throw new RuntimeException("Houve um problema para atualizar o veículo, tente novamente!");
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            if (repository.findById(id).isPresent()) {
                repository.findById(id);
            } else {
                throw new RuntimeException("Usuario com ID: " + id + ", não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var usuarioExistente = repository.findById(id).orElse(null);
            if (usuarioExistente != null) {
                repository.deleteById(id);
            } else {
                throw new RuntimeException("O usuário já foi removido!");
            }
        }
    }

}

