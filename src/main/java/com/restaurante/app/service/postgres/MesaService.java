package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.MesaDTO;
import com.restaurante.domain.dto.MesaDisponivelDTO;
import com.restaurante.domain.entity.MesaEntity;
import com.restaurante.domain.mapper.MesaDisponivelMapper;
import com.restaurante.domain.util.DataFormat;
import com.restaurante.infra.repository.postgres.MesaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
        return new MesaDTO(repository.save(mesaEntity));
    }

    @Transactional(readOnly = true)
    public List<MesaEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<MesaEntity> findAllByRestaurante(Long idRestaurante) {
        return repository.findAllByRestauranteId(idRestaurante);
    }

    @Transactional(readOnly = true)
    public MesaEntity findById(Long id) {
        Optional<MesaEntity> obj = repository.findById(id);
        return obj.orElse(null);
    }

    @Transactional
    public MesaEntity update(Long id, MesaEntity mesaSalvar) {
        Optional<MesaEntity> mesaExistente = repository.findById(id);
        if (mesaExistente.isPresent()) {
            return repository.save(mesaExistente.get());
        } else {
            throw new RuntimeException("Mesa " + id + " não encontrado.");
        }
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<MesaDisponivelDTO> buscarMesas(Long id) {
        return MesaDisponivelMapper.convert(repository.buscarMesasDisponiveis(id, DataFormat.truncate(LocalDateTime.now())));
    }

    public List<MesaDTO> salvarTodasMesas(List<MesaEntity> novasMesasAdicionadas) {
        return repository.saveAll(novasMesasAdicionadas).stream().map(MesaDTO::new).toList();
    }

    public List<MesaEntity> findAllByIdRestaurante(Long idRestaurante) {
        return repository.findAllByRestauranteId(idRestaurante);
    }
}

