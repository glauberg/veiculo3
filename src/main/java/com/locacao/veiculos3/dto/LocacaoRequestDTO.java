package com.locacao.veiculos3.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class LocacaoRequestDTO {

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "ID do veículo é obrigatório")
    private Long veiculoId;

    @NotNull(message = "Data de início é obrigatória")
    private LocalDate dataInicio;

    @NotNull(message = "Data de fim é obrigatória")
    @Future(message = "Data de fim deve ser futura")
    private LocalDate dataFim;

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public Long getVeiculoId() { return veiculoId; }
    public void setVeiculoId(Long veiculoId) { this.veiculoId = veiculoId; }

    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }

    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate dataFim) { this.dataFim = dataFim; }
}