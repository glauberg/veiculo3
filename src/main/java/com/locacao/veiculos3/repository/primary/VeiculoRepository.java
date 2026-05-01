package com.locacao.veiculos3.repository.primary;

import com.locacao.veiculos3.enums.CategoriaVeiculo;
import com.locacao.veiculos3.model.primary.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("SELECT v FROM Veiculo v WHERE v.categoria = :categoria AND v.disponivel = true")
    List<Veiculo> buscarDisponiveisPorCategoria(CategoriaVeiculo categoria);

    @Query(value = "SELECT * FROM veiculos WHERE disponivel = true",
           nativeQuery = true)
    List<Veiculo> buscarTodosDisponiveisNativo();

    @Query("SELECT v FROM Veiculo v LEFT JOIN FETCH v.acessorios WHERE v.id = :id")
    Optional<Veiculo> buscarComAcessorios(Long id);
}