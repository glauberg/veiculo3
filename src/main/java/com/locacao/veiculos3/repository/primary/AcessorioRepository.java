package com.locacao.veiculos3.repository.primary;

import com.locacao.veiculos3.model.primary.Acessorio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcessorioRepository extends JpaRepository<Acessorio, Long> {

    @Query("SELECT a FROM Acessorio a WHERE LOWER(a.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Acessorio> buscarPorNome(String nome);

    @Query(value = "SELECT * FROM acessorios WHERE descricao LIKE '%' || :termo || '%'",
           nativeQuery = true)
    List<Acessorio> buscarPorDescricaoNativo(String termo);
}