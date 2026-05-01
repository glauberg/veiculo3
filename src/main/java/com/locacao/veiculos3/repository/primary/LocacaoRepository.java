package com.locacao.veiculos3.repository.primary;

import com.locacao.veiculos3.model.primary.Locacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocacaoRepository extends JpaRepository<Locacao, Long> {

    List<Locacao> findByClienteId(Long clienteId);
    List<Locacao> findByVeiculoId(Long veiculoId);

    @Query("SELECT l FROM Locacao l WHERE l.valorTotal > :valor")
    List<Locacao> buscarPorValorAcimaDe(double valor);

    @Query(value = "SELECT * FROM locacoes WHERE cliente_id = :clienteId ORDER BY data_inicio DESC",
           nativeQuery = true)
    List<Locacao> buscarLocacoesClienteOrdenadas(Long clienteId);

    @Query("SELECT l FROM Locacao l JOIN FETCH l.cliente JOIN FETCH l.veiculo WHERE l.id = :id")
    Optional<Locacao> buscarComClienteEVeiculo(Long id);
}