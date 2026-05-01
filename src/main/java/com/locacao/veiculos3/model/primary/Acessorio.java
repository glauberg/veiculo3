package com.locacao.veiculos3.model.primary;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "acessorios")
public class Acessorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descricao;

    @ManyToMany(mappedBy = "acessorios")
    private Set<Veiculo> veiculos = new HashSet<>();

    public Acessorio() {}

    public Acessorio(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public Set<Veiculo> getVeiculos() { return veiculos; }
    public void setVeiculos(Set<Veiculo> veiculos) { this.veiculos = veiculos; }
}