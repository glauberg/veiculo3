package com.locacao.veiculos3.service;

import com.locacao.veiculos3.model.audit.LogAuditoria;
import com.locacao.veiculos3.repository.audit.LogAuditoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditoriaService {

    @Autowired
    private LogAuditoriaRepository logRepository;

    @Transactional("auditTransactionManager")
    public void registrar(String entidade, Long entidadeId, String operacao) {
        String usuario = obterUsuarioAutenticado();
        LogAuditoria log = new LogAuditoria(entidade, entidadeId,
                operacao, usuario);
        logRepository.save(log);
    }

    private String obterUsuarioAutenticado() {
        try {
            return SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();
        } catch (Exception e) {
            return "sistema";
        }
    }
}