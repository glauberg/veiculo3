package com.locacao.veiculos3.dto;

import com.locacao.veiculos3.model.primary.Locacao;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LocacaoResponseDTO {

    private Long id;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private double valorTotal;
    private long diasLocacao;
    private String nomeCliente;
    private String modeloVeiculo;
    private String placaVeiculo;

    public LocacaoResponseDTO(Locacao locacao) {
        this.id = locacao.getId();
        this.dataInicio = locacao.getDataInicio();
        this.dataFim = locacao.getDataFim();
        this.valorTotal = locacao.getValorTotal();
        this.diasLocacao = ChronoUnit.DAYS
                .between(locacao.getDataInicio(), locacao.getDataFim());
        this.nomeCliente = locacao.getCliente().getNome();
        this.modeloVeiculo = locacao.getVeiculo().getModelo();
        this.placaVeiculo = locacao.getVeiculo().getPlaca();
    }

    public Long getId() { return id; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getDataFim() { return dataFim; }
    public double getValorTotal() { return valorTotal; }
    public long getDiasLocacao() { return diasLocacao; }
    public String getNomeCliente() { return nomeCliente; }
    public String getModeloVeiculo() { return modeloVeiculo; }
    public String getPlacaVeiculo() { return placaVeiculo; }
}