package com.locacao.veiculos3.controller;

import com.locacao.veiculos3.dto.ClienteRequestDTO;
import com.locacao.veiculos3.dto.ClienteResponseDTO;
import com.locacao.veiculos3.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService service;

    // Nível 3 — MASTER apenas
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_MASTER')")
    public ResponseEntity<ClienteResponseDTO> cadastrar(
            @Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.cadastrar(dto));
    }

    // Nível 1 — todas as roles
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    public ResponseEntity<List<ClienteResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    // Nível 1 — todas as roles
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR','ROLE_AUDITOR')")
    public ResponseEntity<ClienteResponseDTO> buscarPorId(
            @PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Nível 2 — MASTER + CONTRIBUTOR
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_MASTER','ROLE_CONTRIBUTOR')")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequestDTO dto) {
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