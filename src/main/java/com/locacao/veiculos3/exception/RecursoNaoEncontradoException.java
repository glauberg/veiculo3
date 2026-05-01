package com.locacao.veiculos3.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " não encontrado com ID: " + id);
    }
}