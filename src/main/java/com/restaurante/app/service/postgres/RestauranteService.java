package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.RestauranteDTO;
import com.restaurante.domain.entity.RestauranteEntity;
import com.restaurante.domain.useCase.InserirRemoverMesasUseCase;
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

    @Autowired
    public RestauranteService(RestauranteRepository repository, InserirRemoverMesasUseCase insercaoRemocaoDasMesasUseCase) {
        this.repository = repository;
    }

    @Transactional
    public RestauranteDTO save(RestauranteDTO dto) {
        return new RestauranteDTO(repository.save(new RestauranteEntity(dto)));
    }

    @Transactional(readOnly = true)
    public List<RestauranteDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(RestauranteDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public RestauranteDTO findById(Long id) {
        Optional<RestauranteEntity> obj = repository.findById(id);
        return obj.map(RestauranteDTO::new).orElse(null);
    }

    @Transactional
    public RestauranteDTO update(Long id, RestauranteDTO restauranteDtoSalvar) {
        var restauranteExistente = this.findById(id);
        if (restauranteExistente != null) {
            restauranteExistente.setNome(restauranteDtoSalvar.getNome());
            restauranteExistente.setLocalizacao(restauranteDtoSalvar.getLocalizacao());
            restauranteExistente.setTipoCozinha(restauranteDtoSalvar.getTipoCozinha());
            restauranteExistente.setCapacidade(restauranteDtoSalvar.getCapacidade());
            return new RestauranteDTO(repository.save(new RestauranteEntity(restauranteExistente)));
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

