package com.locacao.veiculos3.dto.auth;

import com.locacao.veiculos3.enums.RoleUsuario;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class RegistroRequestDTO {

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 50, message = "Username deve ter entre 3 e 50 caracteres")
    private String username;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotNull(message = "Role é obrigatória")
    private RoleUsuario role;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public RoleUsuario getRole() { return role; }
    public void setRole(RoleUsuario role) { this.role = role; }
}