package com.locacao.veiculos3.service;

import com.locacao.veiculos3.dto.LocacaoRequestDTO;
import com.locacao.veiculos3.dto.LocacaoResponseDTO;
import com.locacao.veiculos3.exception.RecursoNaoEncontradoException;
import com.locacao.veiculos3.exception.RegraNegocioException;
import com.locacao.veiculos3.model.primary.Cliente;
import com.locacao.veiculos3.model.primary.Locacao;
import com.locacao.veiculos3.model.primary.Veiculo;
import com.locacao.veiculos3.repository.primary.ClienteRepository;
import com.locacao.veiculos3.repository.primary.LocacaoRepository;
import com.locacao.veiculos3.repository.primary.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocacaoService {

    @Autowired
    private LocacaoRepository locacaoRepository;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Transactional("primaryTransactionManager")
    public LocacaoResponseDTO cadastrar(LocacaoRequestDTO dto) {
        if (!dto.getDataFim().isAfter(dto.getDataInicio()))
            throw new RegraNegocioException(
                "Data de fim deve ser posterior à data de início");

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Cliente",
                        dto.getClienteId()));

        Veiculo veiculo = veiculoRepository.findById(dto.getVeiculoId())
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Veículo",
                        dto.getVeiculoId()));

        if (!veiculo.isDisponivel())
            throw new RegraNegocioException(
                "Veículo '" + veiculo.getModelo() +
                "' não está disponível para locação");

        long dias = ChronoUnit.DAYS
                .between(dto.getDataInicio(), dto.getDataFim());
        double valorTotal = dias * veiculo.getValorDiaria();

        Locacao locacao = new Locacao();
        locacao.setCliente(cliente);
        locacao.setVeiculo(veiculo);
        locacao.setDataInicio(dto.getDataInicio());
        locacao.setDataFim(dto.getDataFim());
        locacao.setValorTotal(valorTotal);

        veiculo.setDisponivel(false);
        veiculoRepository.save(veiculo);

        Locacao salva = locacaoRepository.save(locacao);
        auditoriaService.registrar("Locacao", salva.getId(), "CREATE");
        return new LocacaoResponseDTO(salva);
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public LocacaoResponseDTO buscarPorId(Long id) {
        return new LocacaoResponseDTO(
                locacaoRepository.buscarComClienteEVeiculo(id)
                        .orElseThrow(() ->
                            new RecursoNaoEncontradoException("Locação", id)));
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<LocacaoResponseDTO> buscarTodos() {
        return locacaoRepository.findAll().stream()
                .map(LocacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<LocacaoResponseDTO> buscarPorCliente(Long clienteId) {
        return locacaoRepository.findByClienteId(clienteId).stream()
                .map(LocacaoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional("primaryTransactionManager")
    public void encerrar(Long id) {
        Locacao locacao = locacaoRepository.buscarComClienteEVeiculo(id)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Locação", id));

        locacao.getVeiculo().setDisponivel(true);
        veiculoRepository.save(locacao.getVeiculo());
        locacaoRepository.delete(locacao);
        auditoriaService.registrar("Locacao", id, "DELETE");
    }
}