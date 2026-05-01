package com.locacao.veiculos3.dto;

import com.locacao.veiculos3.enums.CategoriaVeiculo;
import com.locacao.veiculos3.model.primary.Veiculo;

import java.util.List;
import java.util.stream.Collectors;

public class VeiculoResponseDTO {

    private Long id;
    private String marca;
    private String modelo;
    private String placa;
    private CategoriaVeiculo categoria;
    private boolean disponivel;
    private double valorDiaria;
    private double valorDiariaComTaxa;
    private List<AcessorioResponseDTO> acessorios;

    public VeiculoResponseDTO(Veiculo veiculo) {
        this.id = veiculo.getId();
        this.marca = veiculo.getMarca();
        this.modelo = veiculo.getModelo();
        this.placa = veiculo.getPlaca();
        this.categoria = veiculo.getCategoria();
        this.disponivel = veiculo.isDisponivel();
        this.valorDiaria = veiculo.getValorDiaria();
        this.valorDiariaComTaxa = calcularTaxa(veiculo);
        this.acessorios = veiculo.getAcessorios().stream()
                .map(AcessorioResponseDTO::new)
                .collect(Collectors.toList());
    }

    private double calcularTaxa(Veiculo veiculo) {
        return switch (veiculo.getCategoria()) {
            case ECONOMICO -> veiculo.getValorDiaria() * 1.05;
            case SUV       -> veiculo.getValorDiaria() * 1.15;
            case PREMIUM   -> veiculo.getValorDiaria() * 1.25;
        };
    }

    public Long getId() { return id; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public String getPlaca() { return placa; }
    public CategoriaVeiculo getCategoria() { return categoria; }
    public boolean isDisponivel() { return disponivel; }
    public double getValorDiaria() { return valorDiaria; }
    public double getValorDiariaComTaxa() { return valorDiariaComTaxa; }
    public List<AcessorioResponseDTO> getAcessorios() { return acessorios; }
}