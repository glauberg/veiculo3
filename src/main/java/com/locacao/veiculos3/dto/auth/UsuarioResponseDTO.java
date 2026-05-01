package com.locacao.veiculos3.dto.auth;

import com.locacao.veiculos3.enums.RoleUsuario;
import com.locacao.veiculos3.model.primary.Usuario;

public class UsuarioResponseDTO {

    private Long id;
    private String username;
    private RoleUsuario role;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.username = usuario.getUsername();
        this.role = usuario.getRole();
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public RoleUsuario getRole() { return role; }
}