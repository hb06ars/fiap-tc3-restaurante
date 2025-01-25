package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteRepository repository;

    @Autowired
    public RestauranteService(RestauranteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RestauranteDTO save(RestauranteEntity restauranteEntity) {
        try {
            return new RestauranteDTO(repository.save(restauranteEntity));
        } catch (OptimisticLockingFailureException e) {
            var restauranteExistente = repository.findById(restauranteEntity.getId()).orElse(null);
            if (restauranteExistente != null) {
                return new RestauranteDTO(repository.save(restauranteExistente));
            } else {
                throw new RuntimeException("Houve um problema para a restaurante, tente novamente!");
            }
        }
    }

    @Transactional(readOnly = true)
    public List<RestauranteEntity> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public RestauranteEntity findById(Long id) {
        Optional<RestauranteEntity> obj = repository.findById(id);
        return obj.orElse(null);
    }

    @Transactional
    public RestauranteEntity update(Long id, RestauranteEntity restauranteSalvar) {
        try {
            Optional<RestauranteEntity> restauranteExistente = repository.findById(id);
            if (restauranteExistente.isPresent()) {
                return repository.save(restauranteExistente.get());
            } else {
                throw new RuntimeException("Restaurante " + id + " não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var restauranteExistente = repository.findById(restauranteSalvar.getId()).orElse(null);
            if (restauranteExistente != null) {
                return repository.save(restauranteExistente);
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
                throw new RuntimeException("Restaurante com ID: " + id + ", não encontrado.");
            }
        } catch (OptimisticLockingFailureException e) {
            var restauranteExistente = repository.findById(id).orElse(null);
            if (restauranteExistente != null) {
                repository.deleteById(id);
            } else {
                throw new RuntimeException("O restaurante já foi removido!");
            }
        }
    }

    public List<RestauranteDTO> buscarRestaurantes(String nome, String localizacao, String tipoCozinha) {
        return repository.buscarRestaurantes(
                nome.trim(),
                localizacao.trim(),
                tipoCozinha.trim()).stream().map(RestauranteDTO::new).toList();
    }
}

