package com.locacao.veiculos3.service;

import com.locacao.veiculos3.dto.ClienteRequestDTO;
import com.locacao.veiculos3.dto.ClienteResponseDTO;
import com.locacao.veiculos3.exception.RecursoNaoEncontradoException;
import com.locacao.veiculos3.exception.RegraNegocioException;
import com.locacao.veiculos3.model.primary.Cliente;
import com.locacao.veiculos3.repository.primary.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Transactional("primaryTransactionManager")
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {
        if (repository.findByCpf(dto.getCpf()).isPresent())
            throw new RegraNegocioException(
                "CPF já cadastrado: " + dto.getCpf());

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());

        Cliente salvo = repository.save(cliente);
        auditoriaService.registrar("Cliente", salvo.getId(), "CREATE");
        return new ClienteResponseDTO(salvo);
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public ClienteResponseDTO buscarPorId(Long id) {
        return new ClienteResponseDTO(
                repository.buscarComLocacoes(id)
                        .orElseThrow(() ->
                            new RecursoNaoEncontradoException("Cliente", id)));
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<ClienteResponseDTO> buscarTodos() {
        return repository.findAll().stream()
                .map(ClienteResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional("primaryTransactionManager")
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Cliente", id));
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setTelefone(dto.getTelefone());
        Cliente salvo = repository.save(cliente);
        auditoriaService.registrar("Cliente", salvo.getId(), "UPDATE");
        return new ClienteResponseDTO(salvo);
    }

    @Transactional("primaryTransactionManager")
    public void deletar(Long id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Cliente", id));
        repository.delete(cliente);
        auditoriaService.registrar("Cliente", id, "DELETE");
    }
}