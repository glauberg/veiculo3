package com.locacao.veiculos3.dto;

import com.locacao.veiculos3.model.primary.Cliente;

public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private int totalLocacoes;

    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.cpf = cliente.getCpf();
        this.email = cliente.getEmail();
        this.telefone = cliente.getTelefone();
        this.totalLocacoes = cliente.getLocacoes().size();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
    public String getTelefone() { return telefone; }
    public int getTotalLocacoes() { return totalLocacoes; }
}