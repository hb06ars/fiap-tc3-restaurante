package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.mapper.MesaDisponivelMapper;
import com.restaurante.domain.util.DataFormat;
import com.restaurante.infra.repository.postgres.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MesaService {

    private final MesaRepository repository;

    @Autowired
    public MesaService(MesaRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public MesaDTO save(MesaEntity mesaEntity) {
        try {
            return new MesaDTO(repository.save(mesaEntity));
        } catch (OptimisticLockingFailureException e) {
            var mesaExistente = repository.findById(mesaEntity.getId()).orElse(null);
            if (mesaExistente != null) {
                return new MesaDTO(repository.save(mesaExistente));
            } else {
                throw new RuntimeException("Houve um problema para a mesa, tente novamente!");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<MesaEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public MesaEntity findById(Long id) {
        Optional<MesaEntity> obj = repository.findById(id);
        return obj.orElse(null);
    }

    @Transactional
    public MesaEntity update(Long id, MesaEntity mesaSalvar) {
        try {
            Optional<MesaEntity> mesaExistente = repository.findById(id);
            if (mesaExistente.isPresent()) {
                return repository.save(mesaExistente.get());
            } else {
                throw new RuntimeException("Mesa " + id + " não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var mesaExistente = repository.findById(mesaSalvar.getId()).orElse(null);
            if (mesaExistente != null) {
                return repository.save(mesaExistente);
            } else {
                throw new RuntimeException("Houve um problema para atualizar a mesa, tente novamente!");
            }
        }
    }

    @Transactional
    public void delete(Long id) {
        try {
            if (repository.findById(id).isPresent()) {
                repository.findById(id);
            } else {
                throw new RuntimeException("Mesa com ID: " + id + ", não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var mesaExistente = repository.findById(id).orElse(null);
            if (mesaExistente != null) {
                repository.deleteById(id);
            } else {
                throw new RuntimeException("O mesa já foi removido!");
            }
        }
    }

    public List<MesaDisponivelDTO> buscarMesas(Long id) {
        return MesaDisponivelMapper.convert(repository.buscarMesasDisponiveis(id, DataFormat.truncate(LocalDateTime.now())));
    }
}

