package com.locacao.veiculos3.controller;

import com.locacao.veiculos3.dto.VeiculoRequestDTO;
import com.locacao.veiculos3.dto.VeiculoResponseDTO;
import com.locacao.veiculos3.enums.CategoriaVeiculo;
import com.locacao.veiculos3.service.VeiculoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoService service;

    // Nível 3 — MASTER apenas
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<VeiculoResponseDTO> cadastrar(
            @Valid @RequestBody VeiculoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.cadastrar(dto));
    }

    // Nível 1 — todas as roles
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    public ResponseEntity<List<VeiculoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    // Nível 1 — todas as roles
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    public ResponseEntity<VeiculoResponseDTO> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Nível 1 — todas as roles
    @GetMapping("/disponiveis")
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    public ResponseEntity<List<VeiculoResponseDTO>> buscarDisponiveisPorCategoria(
            @RequestParam CategoriaVeiculo categoria) {
        return ResponseEntity.ok(
                service.buscarDisponiveisPorCategoria(categoria));
    }

    // Nível 2 — MASTER + CONTRIBUTOR
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    public ResponseEntity<VeiculoResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody VeiculoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    // Nível 3 — MASTER apenas
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}