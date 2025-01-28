package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.domain.entity.AvaliacaoEntity;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import com.restaurante.infra.repository.postgres.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepository repository;

    @Autowired
    public AvaliacaoService(AvaliacaoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public AvaliacaoDTO save(AvaliacaoDTO avaliacaoDTO) {
        return new AvaliacaoDTO(repository.save(new AvaliacaoEntity(avaliacaoDTO)));
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoDTO> findAll() {
        return repository.findAll().stream().map(AvaliacaoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public AvaliacaoDTO findById(Long id) {
        Optional<AvaliacaoEntity> obj = repository.findById(id);
        return obj.map(AvaliacaoDTO::new).orElse(null);
    }

    @Transactional
    public AvaliacaoDTO update(Long id, AvaliacaoDTO avaliacaoSalvar) {
        Optional<AvaliacaoEntity> avaliacaoExistente = repository.findById(id);
        if (avaliacaoExistente.isPresent()) {
            return new AvaliacaoDTO(repository.save(avaliacaoExistente.get()));
        } else {
            throw new RuntimeException("Avaliação " + id + " não encontrada.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.findById(id);
        } else {
            throw new ObjectNotFoundException("Avaliacao com ID: " + id + ", não encontrada.");
        }
    }

    public List<AvaliacaoDTO> listarPorRestaurante(Long idRestaurante) {
        return repository.findAllByRestauranteId(idRestaurante).stream().map(AvaliacaoDTO::new).toList();
    }
}

