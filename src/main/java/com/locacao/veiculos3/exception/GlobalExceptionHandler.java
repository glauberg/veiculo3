package com.locacao.veiculos3.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> handleNaoEncontrado(
            RecursoNaoEncontradoException ex) {
        return construirResposta(HttpStatus.NOT_FOUND,
                "Recurso não encontrado", ex.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, Object>> handleRegraNegocio(
            RegraNegocioException ex) {
        return construirResposta(HttpStatus.BAD_REQUEST,
                "Regra de negócio violada", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidacao(
            MethodArgumentNotValidException ex) {
        Map<String, String> campos = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            campos.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        Map<String, Object> erro = new HashMap<>();
        erro.put("timestamp", LocalDateTime.now().toString());
        erro.put("status", 400);
        erro.put("erro", "Erro de validação");
        erro.put("campos", campos);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAcessoNegado(
            AccessDeniedException ex) {
        return construirResposta(HttpStatus.FORBIDDEN,
                "Acesso negado",
                "Você não tem permissão para acessar este recurso");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleNaoAutenticado(
            AuthenticationException ex) {
        return construirResposta(HttpStatus.UNAUTHORIZED,
                "Não autenticado",
                "Token ausente, inválido ou expirado");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleErroGeral(Exception ex) {
        return construirResposta(HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno do servidor", ex.getMessage());
    }

    private ResponseEntity<Map<String, Object>> construirResposta(
            HttpStatus status, String erro, String mensagem) {
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("erro", erro);
        body.put("mensagem", mensagem);
        return ResponseEntity.status(status).body(body);
    }
}