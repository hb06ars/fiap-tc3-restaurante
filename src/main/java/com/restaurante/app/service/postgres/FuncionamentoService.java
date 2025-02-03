package com.restaurante.app.service.postgres;

import com.restaurante.domain.dto.FuncionamentoDTO;
import com.restaurante.domain.entity.FuncionamentoEntity;
import com.restaurante.domain.enums.DiaEnum;
import com.restaurante.infra.exceptions.ObjectNotFoundException;
import com.restaurante.infra.repository.postgres.FuncionamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FuncionamentoService {

    @Value("${tolerancia-mesa}")
    private Integer toleranciaMesa;

    private final FuncionamentoRepository repository;

    @Autowired
    public FuncionamentoService(FuncionamentoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public FuncionamentoDTO save(FuncionamentoDTO funcionamentoDTO) {
        return new FuncionamentoDTO(repository.save(new FuncionamentoEntity(funcionamentoDTO)));
    }

    @Transactional(readOnly = true)
    public List<FuncionamentoDTO> findAll() {
        return repository.findAll().stream().map(FuncionamentoDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public FuncionamentoDTO findById(Long id) {
        Optional<FuncionamentoEntity> obj = repository.findById(id);
        return obj.map(FuncionamentoDTO::new).orElse(null);
    }

    @Transactional
    public FuncionamentoDTO update(Long id, FuncionamentoDTO dto) {
        Optional<FuncionamentoEntity> funcionamentoExistente = repository.findById(id);
        if (funcionamentoExistente.isPresent()) {
            funcionamentoExistente.get().setAbertura(dto.getAbertura());
            funcionamentoExistente.get().setFechamento(dto.getFechamento().plusHours(toleranciaMesa));
            funcionamentoExistente.get().setDiaEnum(dto.getDiaEnum());
            funcionamentoExistente.get().setRestauranteId(dto.getRestauranteId());
            return new FuncionamentoDTO(repository.save(funcionamentoExistente.get()));
        } else {
            throw new ObjectNotFoundException("Funcionamento " + id + " não encontrado.");
        }
    }

    @Transactional
    public void delete(Long id) {
        if (repository.findById(id).isPresent()) {
            repository.deleteById(id);
        } else {
            throw new ObjectNotFoundException("Funcionamento com ID: " + id + ", não encontrado.");
        }
    }

    public List<FuncionamentoDTO> validarDataFuncionamento(Long restauranteId,
                                                           LocalDateTime dataReserva, DiaEnum diaenum) {
        return repository.validarData(restauranteId, dataReserva, diaenum.name())
                .stream()
                .map(FuncionamentoDTO::new).toList();
    }

    public List<FuncionamentoDTO> buscarPorRestaurante(Long idRestaurante) {
        return repository.findAllByRestauranteId(idRestaurante).stream().map(FuncionamentoDTO::new).toList();
    }

    public void inserirDataFuncionamentoInicial(Long idRestaurante) {
        List<DiaEnum> diaEnumList = getDiasUteisCorridos();
        List<FuncionamentoDTO> funcionamentoDTOList = new ArrayList<>();
        diaEnumList.forEach(diaEnumAtual -> {
            funcionamentoDTOList.add(FuncionamentoDTO.builder()
                    .abertura(LocalTime.parse("00:00:00"))
                    .fechamento(LocalTime.parse("23:00:00"))
                    .restauranteId(idRestaurante)
                    .diaEnum(diaEnumAtual)
                    .build());
        });
        repository.saveAll(funcionamentoDTOList.stream()
                .map(FuncionamentoEntity::new)
                .toList());
    }

    private static List<DiaEnum> getDiasUteisCorridos() {
        return List.of(
                DiaEnum.FERIADOS,
                DiaEnum.DOMINGO,
                DiaEnum.SEGUNDA,
                DiaEnum.TERCA,
                DiaEnum.QUARTA,
                DiaEnum.QUINTA,
                DiaEnum.SEXTA,
                DiaEnum.SABADO
        );
    }
}

