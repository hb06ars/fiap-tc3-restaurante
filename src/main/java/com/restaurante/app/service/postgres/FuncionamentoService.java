package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.domain.util.DataFormat;
import com.restaurante.infra.repository.postgres.FuncionamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionamentoService {

    private final FuncionamentoRepository repository;

    @Autowired
    public FuncionamentoService(FuncionamentoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public FuncionamentoDTO save(FuncionamentoEntity funcionamentoEntity) {
        return new FuncionamentoDTO(repository.save(funcionamentoEntity));
    }

    @Transactional(readOnly = true)
    public List<FuncionamentoEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public FuncionamentoEntity findById(Long id) {
        Optional<FuncionamentoEntity> obj = repository.findById(id);
        return obj.orElse(null);
    }

    @Transactional
    public FuncionamentoEntity update(Long id, FuncionamentoEntity funcionamentoSalvar) {
        Optional<FuncionamentoEntity> funcionamentoExistente = repository.findById(id);
        if (funcionamentoExistente.isPresent()) {
            return repository.save(funcionamentoExistente.get());
        } else {
            throw new RuntimeException("Funcionamento " + id + " não encontrado.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new RuntimeException("Funcionamento com ID: " + id + ", não encontrado.");
        }
    }

    public List<FuncionamentoEntity> buscarMesasDisponiveis(Long restauranteId, LocalDateTime dataReserva, DiaEnum diaenum) {
        return repository.validarData(restauranteId, DataFormat.truncate(dataReserva), diaenum.name());
    }
}

