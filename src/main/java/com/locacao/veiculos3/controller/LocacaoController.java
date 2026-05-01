package com.locacao.veiculos3.controller;

import com.locacao.veiculos3.dto.LocacaoRequestDTO;
import com.locacao.veiculos3.dto.LocacaoResponseDTO;
import com.locacao.veiculos3.service.LocacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/locacoes")
public class LocacaoController {

    @Autowired
    private LocacaoService service;

    // Nível 2 — MASTER + CONTRIBUTOR
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    public ResponseEntity<LocacaoResponseDTO> cadastrar(
            @Valid @RequestBody LocacaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.cadastrar(dto));
    }

    // Nível 1 — todas as roles
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    public ResponseEntity<List<LocacaoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    // Nível 1 — todas as roles
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    public ResponseEntity<LocacaoResponseDTO> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Nível 1 — todas as roles
    @GetMapping("/cliente/{clienteId}")
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    public ResponseEntity<List<LocacaoResponseDTO>> buscarPorCliente(
            @PathVariable Long clienteId) {
        return ResponseEntity.ok(service.buscarPorCliente(clienteId));
    }

    // Nível 2 — MASTER + CONTRIBUTOR
    @DeleteMapping("/{id}/encerrar")
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    public ResponseEntity<Void> encerrar(@PathVariable Long id) {
        service.encerrar(id);
        return ResponseEntity.noContent().build();
    }
}