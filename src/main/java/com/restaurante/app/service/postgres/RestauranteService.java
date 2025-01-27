package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.useCase.InsercaoRemocaoDasMesasUseCase;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import com.restaurante.infra.repository.postgres.RestauranteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RestauranteService {

    private final RestauranteRepository repository;
    private final InsercaoRemocaoDasMesasUseCase insercaoRemocaoDasMesasUseCase;

    @Autowired
    public RestauranteService(RestauranteRepository repository, InsercaoRemocaoDasMesasUseCase insercaoRemocaoDasMesasUseCase) {
        this.repository = repository;
        this.insercaoRemocaoDasMesasUseCase = insercaoRemocaoDasMesasUseCase;
    }

    @Transactional
    public RestauranteDTO save(RestauranteEntity restauranteEntity) {
        return new RestauranteDTO(repository.save(restauranteEntity));
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
    public RestauranteDTO update(Long id, RestauranteEntity restauranteSalvar) {
        var restauranteExistente = findById(id);
        if (restauranteExistente != null) {
            restauranteExistente.setNome(restauranteSalvar.getNome());
            restauranteExistente.setLocalizacao(restauranteSalvar.getLocalizacao());
            restauranteExistente.setTipoCozinha(restauranteSalvar.getTipoCozinha());
            restauranteExistente.setCapacidade(restauranteSalvar.getCapacidade());
            return new RestauranteDTO(repository.save(restauranteExistente));
        } else {
            throw new ObjectNotFoundException("Restaurante não encontrado no sistema!");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.findById(id);
        } else {
            throw new RuntimeException("Restaurante com ID: " + id + ", não encontrado.");
        }
    }

    public List<RestauranteDTO> buscarRestaurantes(String nome, String localizacao, String tipoCozinha) {
        return repository.buscarRestaurantes(
                nome.trim(),
                localizacao.trim(),
                tipoCozinha.trim()).stream().map(RestauranteDTO::new).toList();
    }

    public boolean restauranteJaExiste(String nome, String localizacao) {
        return repository.buscarRestaurantes(
                nome.trim(),
                localizacao.trim(),
                null).stream().map(RestauranteDTO::new).findAny().isPresent();
    }
}

