package com.locacao.veiculos3.dto.auth;

public class LoginResponseDTO {

    private String token;
    private String username;
    private String role;
    private long expiracaoEm;

    public LoginResponseDTO(String token, String username,
                            String role, long expiracaoEm) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.expiracaoEm = expiracaoEm;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public long getExpiracaoEm() { return expiracaoEm; }
}