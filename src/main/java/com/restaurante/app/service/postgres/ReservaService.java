package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.infra.repository.postgres.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReservaService {

    private final ReservaRepository repository;

    @Autowired
    public ReservaService(ReservaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ReservaDTO save(ReservaEntity reservaEntity) {
        try {
            return new ReservaDTO(repository.save(reservaEntity));
        } catch (OptimisticLockingFailureException e) {
            var reservaExistente = repository.findById(reservaEntity.getId()).orElse(null);
            if (reservaExistente != null) {
                return new ReservaDTO(repository.save(reservaExistente));
            } else {
                throw new RuntimeException("Houve um problema para a reserva, tente novamente!");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<ReservaEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public ReservaEntity findById(Long id) {
        Optional<ReservaEntity> obj = repository.findById(id);
        return obj.orElse(null);
    }

    @Transactional
    public ReservaEntity update(Long id, ReservaEntity reservaSalvar) {
        try {
            Optional<ReservaEntity> reservaExistente = repository.findById(id);
            if (reservaExistente.isPresent()) {
                return repository.save(reservaExistente.get());
            } else {
                throw new RuntimeException("Reserva " + id + " não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var reservaExistente = repository.findById(reservaSalvar.getId()).orElse(null);
            if (reservaExistente != null) {
                return repository.save(reservaExistente);
            } else {
                throw new RuntimeException("Houve um problema para atualizar o registro, tente novamente!");
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            if (repository.findById(id).isPresent()) {
                repository.findById(id);
            } else {
                throw new RuntimeException("Reserva com ID: " + id + ", não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var reservaExistente = repository.findById(id).orElse(null);
            if (reservaExistente != null) {
                repository.deleteById(id);
            } else {
                throw new RuntimeException("A reserva já foi removida!");
            }
        }
    }

}

