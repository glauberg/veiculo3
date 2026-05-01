package com.locacao.veiculos3.service;

import com.locacao.veiculos3.dto.VeiculoRequestDTO;
import com.locacao.veiculos3.dto.VeiculoResponseDTO;
import com.locacao.veiculos3.enums.CategoriaVeiculo;
import com.locacao.veiculos3.exception.RecursoNaoEncontradoException;
import com.locacao.veiculos3.exception.RegraNegocioException;
import com.locacao.veiculos3.model.primary.Acessorio;
import com.locacao.veiculos3.model.primary.Veiculo;
import com.locacao.veiculos3.repository.primary.AcessorioRepository;
import com.locacao.veiculos3.repository.primary.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private AcessorioRepository acessorioRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Transactional("primaryTransactionManager")
    public VeiculoResponseDTO cadastrar(VeiculoRequestDTO dto) {
        validarValorDiariaPorCategoria(dto.getCategoria(), dto.getValorDiaria());

        Veiculo veiculo = new Veiculo();
        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setCategoria(dto.getCategoria());
        veiculo.setValorDiaria(dto.getValorDiaria());
        veiculo.setDisponivel(true);

        if (dto.getAcessorioIds() != null && !dto.getAcessorioIds().isEmpty()) {
            Set<Acessorio> acessorios = new HashSet<>(
                acessorioRepository.findAllById(dto.getAcessorioIds()));
            veiculo.setAcessorios(acessorios);
        }

        Veiculo salvo = veiculoRepository.save(veiculo);
        auditoriaService.registrar("Veiculo", salvo.getId(), "CREATE");
        return new VeiculoResponseDTO(salvo);
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public VeiculoResponseDTO buscarPorId(Long id) {
        return new VeiculoResponseDTO(
                veiculoRepository.buscarComAcessorios(id)
                        .orElseThrow(() ->
                            new RecursoNaoEncontradoException("Veículo", id)));
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<VeiculoResponseDTO> buscarTodos() {
        return veiculoRepository.findAll().stream()
                .map(VeiculoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<VeiculoResponseDTO> buscarDisponiveisPorCategoria(
            CategoriaVeiculo categoria) {
        return veiculoRepository.buscarDisponiveisPorCategoria(categoria)
                .stream()
                .map(VeiculoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional("primaryTransactionManager")
    public VeiculoResponseDTO atualizar(Long id, VeiculoRequestDTO dto) {
        Veiculo veiculo = veiculoRepository.buscarComAcessorios(id)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Veículo", id));

        validarValorDiariaPorCategoria(dto.getCategoria(), dto.getValorDiaria());

        veiculo.setMarca(dto.getMarca());
        veiculo.setModelo(dto.getModelo());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setCategoria(dto.getCategoria());
        veiculo.setValorDiaria(dto.getValorDiaria());

        if (dto.getAcessorioIds() != null) {
            Set<Acessorio> acessorios = new HashSet<>(
                acessorioRepository.findAllById(dto.getAcessorioIds()));
            veiculo.setAcessorios(acessorios);
        }

        Veiculo salvo = veiculoRepository.save(veiculo);
        auditoriaService.registrar("Veiculo", salvo.getId(), "UPDATE");
        return new VeiculoResponseDTO(salvo);
    }

    @Transactional("primaryTransactionManager")
    public void deletar(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Veículo", id));
        veiculoRepository.delete(veiculo);
        auditoriaService.registrar("Veiculo", id, "DELETE");
    }

    private void validarValorDiariaPorCategoria(
            CategoriaVeiculo categoria, double valor) {
        switch (categoria) {
            case ECONOMICO -> {
                if (valor < 50)
                    throw new RegraNegocioException(
                        "Valor mínimo para ECONOMICO é R$ 50,00");
            }
            case SUV -> {
                if (valor < 150)
                    throw new RegraNegocioException(
                        "Valor mínimo para SUV é R$ 150,00");
            }
            case PREMIUM -> {
                if (valor < 300)
                    throw new RegraNegocioException(
                        "Valor mínimo para PREMIUM é R$ 300,00");
            }
        }
    }
}