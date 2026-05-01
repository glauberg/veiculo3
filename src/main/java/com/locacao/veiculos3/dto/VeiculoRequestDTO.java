package com.locacao.veiculos3.dto;

import com.locacao.veiculos3.enums.CategoriaVeiculo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class VeiculoRequestDTO {

    @NotBlank(message = "Marca é obrigatória")
    private String marca;

    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;

    @NotBlank(message = "Placa é obrigatória")
    private String placa;

    @NotNull(message = "Categoria é obrigatória")
    private CategoriaVeiculo categoria;

    @Min(value = 1, message = "Valor da diária deve ser maior que zero")
    private double valorDiaria;

    private List<Long> acessorioIds;

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }

    public CategoriaVeiculo getCategoria() { return categoria; }
    public void setCategoria(CategoriaVeiculo categoria) { this.categoria = categoria; }

    public double getValorDiaria() { return valorDiaria; }
    public void setValorDiaria(double valorDiaria) { this.valorDiaria = valorDiaria; }

    public List<Long> getAcessorioIds() { return acessorioIds; }
    public void setAcessorioIds(List<Long> acessorioIds) { this.acessorioIds = acessorioIds; }
}