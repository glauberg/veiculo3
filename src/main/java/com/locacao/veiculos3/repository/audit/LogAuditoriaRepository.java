package com.locacao.veiculos3.repository.audit;

import com.locacao.veiculos3.model.audit.LogAuditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogAuditoriaRepository extends JpaRepository<LogAuditoria, Long> {

    List<LogAuditoria> findByEntidade(String entidade);
    List<LogAuditoria> findByUsuarioResponsavel(String username);

    @Query("SELECT l FROM LogAuditoria l WHERE l.operacao = :operacao ORDER BY l.dataHora DESC")
    List<LogAuditoria> buscarPorOperacao(String operacao);

    @Query(value = "SELECT * FROM log_auditoria ORDER BY data_hora DESC LIMIT 50",
           nativeQuery = true)
    List<LogAuditoria> buscarUltimos50Logs();
}