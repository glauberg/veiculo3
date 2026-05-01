package com.locacao.veiculos3.dto;

import com.locacao.veiculos3.model.primary.Acessorio;

public class AcessorioResponseDTO {

    private Long id;
    private String nome;
    private String descricao;

    public AcessorioResponseDTO(Acessorio acessorio) {
        this.id = acessorio.getId();
        this.nome = acessorio.getNome();
        this.descricao = acessorio.getDescricao();
    }

    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
}