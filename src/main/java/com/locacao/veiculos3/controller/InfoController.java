package com.locacao.veiculos3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/info")
public class InfoController {

    @GetMapping
    public ResponseEntity<Map<String, Object>> info() {
        Map<String, Object> info = Map.of(
            "sistema", "Sistema de Locação de Veículos",
            "versao", "3.0.0",
            "descricao", "API REST para gerenciamento de frota e locações",
            "disciplina", "PPGTI 1004 - Desenvolvimento Web II",
            "roles", Map.of(
                "ROLE_MASTER", "Acesso total ao sistema",
                "ROLE_CONTRIBUTOR", "Criação e manipulação de recursos",
                "ROLE_AUDITOR", "Somente leitura"
            )
        );
        return ResponseEntity.ok(info);
    }
}