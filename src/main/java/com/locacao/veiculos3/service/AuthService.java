package com.locacao.veiculos3.service;

import com.locacao.veiculos3.dto.auth.LoginRequestDTO;
import com.locacao.veiculos3.dto.auth.LoginResponseDTO;
import com.locacao.veiculos3.dto.auth.RegistroRequestDTO;
import com.locacao.veiculos3.dto.auth.UsuarioResponseDTO;
import com.locacao.veiculos3.exception.RegraNegocioException;
import com.locacao.veiculos3.model.primary.Usuario;
import com.locacao.veiculos3.repository.primary.UsuarioRepository;
import com.locacao.veiculos3.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.expiration}")
    private long expiration;

    @Transactional("primaryTransactionManager")
    public UsuarioResponseDTO registrar(RegistroRequestDTO dto) {
        if (usuarioRepository.findByUsername(dto.getUsername()).isPresent())
            throw new RegraNegocioException(
                "Username já cadastrado: " + dto.getUsername());

        Usuario usuario = new Usuario(
                dto.getUsername(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getRole()
        );

        return new UsuarioResponseDTO(usuarioRepository.save(usuario));
    }

    public LoginResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsername(), dto.getPassword())
        );

        Usuario usuario = usuarioRepository
                .findByUsername(dto.getUsername())
                .orElseThrow(() -> new RegraNegocioException(
                    "Usuário não encontrado"));

        String token = jwtUtil.gerarToken(
                usuario.getUsername(),
                usuario.getRole().name()
        );

        return new LoginResponseDTO(
                token,
                usuario.getUsername(),
                usuario.getRole().name(),
                expiration
        );
    }
}