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
    public MesaDTO save(MesaDTO dto) {
        return new MesaDTO(repository.save(new MesaEntity(dto)));
    }

    @Transactional(readOnly = true)
    public List<MesaDTO> findAll() {
        return repository.findAll().stream().map(MesaDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public List<MesaDTO> findAllByRestaurante(Long idRestaurante) {
        return repository.findAllByRestauranteId(idRestaurante).stream().map(MesaDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public MesaDTO findById(Long id) {
        Optional<MesaEntity> obj = repository.findById(id);
        return obj.map(MesaDTO::new).orElse(null);
    }

    @Transactional
    public MesaDTO update(Long id, MesaDTO dto) {
        Optional<MesaEntity> mesaExistente = repository.findById(id);
        if (mesaExistente.isPresent()) {
            mesaExistente.get().setNomeMesa(dto.getNomeMesa());
            mesaExistente.get().setRestauranteId(dto.getRestauranteId());
            return new MesaDTO(repository.save(mesaExistente.get()));
        } else {
            throw new RuntimeException("Mesa " + id + " não encontrada.");
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

    public List<MesaDTO> findAllByIdRestaurante(Long idRestaurante) {
        return repository.findAllByRestauranteId(idRestaurante).stream().map(MesaDTO::new).toList();
    }
}

