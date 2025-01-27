package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.util.DataFormat;
import com.restaurante.infra.exceptions.FieldNotFoundException;
import com.restaurante.infra.repository.postgres.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new ReservaDTO(repository.save(reservaEntity));
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
        Optional<ReservaEntity> reservaExistente = repository.findById(id);
        if (reservaExistente.isPresent()) {
            return repository.save(reservaExistente.get());
        } else {
            throw new RuntimeException("Reserva " + id + " não encontrado.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Reserva com ID: " + id + ", não encontrado.");
        }
    }

    public List<ReservaDTO> buscar(Long idrestaurante, ReservaEntity entity) {
        if (entity.getDataDaReserva() == null)
            throw new FieldNotFoundException("Informe a data.");
        return repository.findAllByFilter(
                idrestaurante,
                entity.getStatusReserva() != null ? entity.getStatusReserva().toString() : "",
                entity.getStatusPagamento() != null ? entity.getStatusPagamento().toString() : "",
                DataFormat.truncate(entity.getDataDaReserva())
        ).stream().map(ReservaDTO::new).toList();
    }
}

