package com.locacao.veiculos3.model.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_auditoria")
public class LogAuditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String entidade;

    @Column(nullable = false)
    private Long entidadeId;

    @Column(nullable = false)
    private String operacao;

    @Column(nullable = false)
    private String usuarioResponsavel;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    public LogAuditoria() {}

    public LogAuditoria(String entidade, Long entidadeId,
                        String operacao, String usuarioResponsavel) {
        this.entidade = entidade;
        this.entidadeId = entidadeId;
        this.operacao = operacao;
        this.usuarioResponsavel = usuarioResponsavel;
        this.dataHora = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEntidade() { return entidade; }
    public void setEntidade(String entidade) { this.entidade = entidade; }

    public Long getEntidadeId() { return entidadeId; }
    public void setEntidadeId(Long entidadeId) { this.entidadeId = entidadeId; }

    public String getOperacao() { return operacao; }
    public void setOperacao(String operacao) { this.operacao = operacao; }

    public String getUsuarioResponsavel() { return usuarioResponsavel; }
    public void setUsuarioResponsavel(String u) { this.usuarioResponsavel = u; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
}