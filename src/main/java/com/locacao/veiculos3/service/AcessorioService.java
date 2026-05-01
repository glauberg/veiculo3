package com.locacao.veiculos3.service;

import com.locacao.veiculos3.dto.AcessorioRequestDTO;
import com.locacao.veiculos3.dto.AcessorioResponseDTO;
import com.locacao.veiculos3.exception.RecursoNaoEncontradoException;
import com.locacao.veiculos3.model.primary.Acessorio;
import com.locacao.veiculos3.repository.primary.AcessorioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AcessorioService {

    @Autowired
    private AcessorioRepository repository;

    @Autowired
    private AuditoriaService auditoriaService;

    @Transactional("primaryTransactionManager")
    public AcessorioResponseDTO cadastrar(AcessorioRequestDTO dto) {
        Acessorio acessorio = new Acessorio(dto.getNome(), dto.getDescricao());
        Acessorio salvo = repository.save(acessorio);
        auditoriaService.registrar("Acessorio", salvo.getId(), "CREATE");
        return new AcessorioResponseDTO(salvo);
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public AcessorioResponseDTO buscarPorId(Long id) {
        return new AcessorioResponseDTO(repository.findById(id)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Acessório", id)));
    }

    @Transactional(value = "primaryTransactionManager", readOnly = true)
    public List<AcessorioResponseDTO> buscarTodos() {
        return repository.findAll().stream()
                .map(AcessorioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional("primaryTransactionManager")
    public AcessorioResponseDTO atualizar(Long id, AcessorioRequestDTO dto) {
        Acessorio acessorio = repository.findById(id)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Acessório", id));
        acessorio.setNome(dto.getNome());
        acessorio.setDescricao(dto.getDescricao());
        Acessorio salvo = repository.save(acessorio);
        auditoriaService.registrar("Acessorio", salvo.getId(), "UPDATE");
        return new AcessorioResponseDTO(salvo);
    }

    @Transactional("primaryTransactionManager")
    public void deletar(Long id) {
        Acessorio acessorio = repository.findById(id)
                .orElseThrow(() ->
                    new RecursoNaoEncontradoException("Acessório", id));
        repository.delete(acessorio);
        auditoriaService.registrar("Acessorio", id, "DELETE");
    }
}