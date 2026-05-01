package com.locacao.veiculos3.model.primary;

import com.locacao.veiculos3.enums.CategoriaVeiculo;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marca;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false, unique = true)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaVeiculo categoria;

    @Column(nullable = false)
    private boolean disponivel = true;

    @Column(nullable = false)
    private double valorDiaria;

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Locacao> locacoes = new ArrayList<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "veiculo_acessorio",
        joinColumns = @JoinColumn(name = "veiculo_id"),
        inverseJoinColumns = @JoinColumn(name = "acessorio_id")
    )
    private Set<Acessorio> acessorios = new HashSet<>();

    public Veiculo() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public CategoriaVeiculo getCategoria() { return categoria; }
    public void setCategoria(CategoriaVeiculo categoria) { this.categoria = categoria; }

    public boolean isDisponivel() { return disponivel; }
    public void setDisponivel(boolean disponivel) { this.disponivel = disponivel; }

    public double getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(double valorDiaria) { this.valorDiaria = valorDiaria; }

    public List<Locacao> getLocacoes() { return locacoes; }
    public void setLocacoes(List<Locacao> locacoes) { this.locacoes = locacoes; }

    public Set<Acessorio> getAcessorios() { return acessorios; }
    public void setAcessorios(Set<Acessorio> acessorios) { this.acessorios = acessorios; }
}