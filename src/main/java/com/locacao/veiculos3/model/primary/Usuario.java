package com.locacao.veiculos3.model.primary;

import com.locacao.veiculos3.enums.RoleUsuario;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleUsuario role;

    public Usuario() {}

    public Usuario(String username, String password, RoleUsuario role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public RoleUsuario getRole() { return role; }
    public void setRole(RoleUsuario role) { this.role = role; }
}