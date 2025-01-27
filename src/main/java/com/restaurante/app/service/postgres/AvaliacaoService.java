package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.domain.entity.AvaliacaoEntity;
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
    public AvaliacaoDTO save(AvaliacaoEntity avaliacaoEntity) {
        return new AvaliacaoDTO(repository.save(avaliacaoEntity));
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public AvaliacaoEntity findById(Long id) {
        Optional<AvaliacaoEntity> obj = repository.findById(id);
        return obj.orElse(null);
    }

    @Transactional
    public AvaliacaoEntity update(Long id, AvaliacaoEntity avaliacaoSalvar) {
        Optional<AvaliacaoEntity> avaliacaoExistente = repository.findById(id);
        if (avaliacaoExistente.isPresent()) {
            return repository.save(avaliacaoExistente.get());
        } else {
            throw new RuntimeException("Avaliação " + id + " não encontrada.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.findById(id);
        } else {
            throw new RuntimeException("Avaliacao com ID: " + id + ", não encontrada.");
        }
    }

}

