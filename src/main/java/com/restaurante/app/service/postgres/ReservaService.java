package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.ReservaDTO;
import com.restaurante.domain.entity.ReservaEntity;
import com.restaurante.domain.util.DataFormat;
import com.restaurante.infra.exceptions.FieldNotFoundException;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
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
    public ReservaDTO save(ReservaDTO dto) {
        return new ReservaDTO(repository.save(new ReservaEntity(dto)));
    }

    @Transactional(readOnly = true)
    public List<ReservaDTO> findAll() {
        return repository.findAll()
                .stream()
                .map(ReservaDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReservaDTO findById(Long id) {
        Optional<ReservaEntity> obj = repository.findById(id);
        return obj.map(ReservaDTO::new).orElse(null);
    }

    @Transactional
    public ReservaDTO update(Long id, ReservaDTO reservaSalvar) {
        Optional<ReservaEntity> reservaExistente = repository.findById(id);
        if (reservaExistente.isPresent()) {
            reservaExistente.get().setValorReserva(reservaSalvar.getValorReserva());
            reservaExistente.get().setStatusReserva(reservaSalvar.getStatusReserva());
            reservaExistente.get().setDataDaReserva(reservaSalvar.getDataDaReserva());
            reservaExistente.get().setDataFimReserva(reservaSalvar.getDataFimReserva());
            reservaExistente.get().setUsuarioId(reservaSalvar.getUsuarioId());
            reservaExistente.get().setStatusPagamento(reservaSalvar.getStatusPagamento());
            return new ReservaDTO(repository.save(reservaExistente.get()));
        } else {
            throw new ObjectNotFoundException("Reserva " + id + " não encontrada.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("Reserva com ID: " + id + ", não encontrada.");
        }
    }

    public List<ReservaDTO> buscar(Long idrestaurante, ReservaDTO dto) {
        if (dto.getDataDaReserva() == null)
            throw new FieldNotFoundException("Informe a data.");
        return repository.findAllByFilter(
                idrestaurante,
                dto.getStatusReserva(),
                dto.getStatusPagamento(),
                DataFormat.truncate(dto.getDataDaReserva())
        ).stream().map(ReservaDTO::new).toList();
    }
}

