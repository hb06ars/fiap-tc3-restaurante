package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.AvaliacaoDTO;
import com.restaurante.domain.entity.AvaliacaoEntity;
import com.restaurante.infra.repository.postgres.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
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
        try {
            return new AvaliacaoDTO(repository.save(avaliacaoEntity));
        } catch (OptimisticLockingFailureException e) {
            var avaliacaoExistente = repository.findById(avaliacaoEntity.getId()).orElse(null);
            if (avaliacaoExistente != null) {
                return new AvaliacaoDTO(repository.save(avaliacaoExistente));
            } else {
                throw new RuntimeException("Houve um problema para a avaliacao, tente novamente!");
            }
        }
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
        try {
            Optional<AvaliacaoEntity> avaliacaoExistente = repository.findById(id);
            if (avaliacaoExistente.isPresent()) {
                return repository.save(avaliacaoExistente.get());
            } else {
                throw new RuntimeException("Avaliacao " + id + " não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var avaliacaoExistente = repository.findById(avaliacaoSalvar.getId()).orElse(null);
            if (avaliacaoExistente != null) {
                return repository.save(avaliacaoExistente);
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
                throw new RuntimeException("Avaliacao com ID: " + id + ", não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var avaliacaoExistente = repository.findById(id).orElse(null);
            if (avaliacaoExistente != null) {
                repository.deleteById(id);
            } else {
                throw new RuntimeException("A avaliação já foi removida!");
            }
        }
    }

}

