package com.locacao.veiculos3.controller;

import com.locacao.veiculos3.dto.auth.LoginRequestDTO;
import com.locacao.veiculos3.dto.auth.LoginResponseDTO;
import com.locacao.veiculos3.dto.auth.RegistroRequestDTO;
import com.locacao.veiculos3.dto.auth.UsuarioResponseDTO;
import com.locacao.veiculos3.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    // Público — qualquer um pode fazer login
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    // Público — permite criar o primeiro usuário MASTER
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrar(
            @Valid @RequestBody RegistroRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registrar(dto));
    }
}