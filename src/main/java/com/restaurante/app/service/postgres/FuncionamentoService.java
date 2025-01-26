package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.enums.DiaEnum;
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
        try {
            return new FuncionamentoDTO(repository.save(funcionamentoEntity));
        } catch (OptimisticLockingFailureException e) {
            var funcionamentoExistente = repository.findById(funcionamentoEntity.getId()).orElse(null);
            if (funcionamentoExistente != null) {
                return new FuncionamentoDTO(repository.save(funcionamentoExistente));
            } else {
                throw new RuntimeException("Houve um problema para a funcionamento, tente novamente!");
            }
        }
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
        try {
            Optional<FuncionamentoEntity> funcionamentoExistente = repository.findById(id);
            if (funcionamentoExistente.isPresent()) {
                return repository.save(funcionamentoExistente.get());
            } else {
                throw new RuntimeException("Funcionamento " + id + " não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var funcionamentoExistente = repository.findById(funcionamentoSalvar.getId()).orElse(null);
            if (funcionamentoExistente != null) {
                return repository.save(funcionamentoExistente);
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
                throw new RuntimeException("Funcionamento com ID: " + id + ", não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var funcionamentoExistente = repository.findById(id).orElse(null);
            if (funcionamentoExistente != null) {
                repository.deleteById(id);
            } else {
                throw new RuntimeException("O funcionamento já foi removido!");
            }
        }
    }

    public List<FuncionamentoEntity> buscarMesasDisponiveis(Long restauranteId, LocalDateTime dataReserva, DiaEnum diaenum) {
        return repository.validarData(restauranteId, dataReserva, diaenum.name());
    }
}

