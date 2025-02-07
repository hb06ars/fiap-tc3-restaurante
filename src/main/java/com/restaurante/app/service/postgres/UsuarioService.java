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
import java.util.Objects;
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
        UsuarioEntity usuarioExiste = repository.findByEmailOrCelular(dto.getEmail(),
                AjustesString.removerCaracteresCel(dto.getCelular()));
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
        UsuarioEntity usuarioExistente = repository.findById(id).orElse(null);
        var registroRepetido = repository.findByEmailOrCelular(usuarioSalvar.getEmail(),
                AjustesString.removerCaracteresCel(usuarioSalvar.getCelular()));
        if (usuarioExistente != null && (registroRepetido == null ||
                registroRepetido.getId().equals(Objects.requireNonNull(usuarioExistente).getId()))) {
            usuarioExistente.setNome(usuarioSalvar.getNome());
            usuarioExistente.setEmail(usuarioSalvar.getEmail());
            usuarioExistente.setCelular(usuarioSalvar.getCelular());
            return new UsuarioDTO(repository.save(usuarioExistente));
        } else {
            throw new RuntimeException("Usuário " + id + " não encontrado.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Usuário com ID: " + id + ", não encontrado.");
        }
    }

    public UsuarioDTO findByEmailOrCelular(String email, String celular) {
        var usuarioEncontrado = repository.findByEmailOrCelular(
                email != null && !email.isBlank() ? email.trim() : null,
                celular != null && !celular.isBlank() ? AjustesString.removerCaracteresCel(celular.trim()) : null);

        if (usuarioEncontrado == null)
            throw new RuntimeException("Usuário não encontrado.");
        return new UsuarioDTO(usuarioEncontrado);
    }
}

