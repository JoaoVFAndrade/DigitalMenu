package com.br.digitalmenu.repository;

import com.br.digitalmenu.dto.ProdutoDashboardDTO;
import com.br.digitalmenu.dto.RelatorioVendasDeProdutoDTO;
import com.br.digitalmenu.model.ProdutoPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ProdutoPedidoRepository extends JpaRepository<ProdutoPedido, Long> {
    @Query("""
                SELECT new com.br.digitalmenu.dto.ProdutoDashboardDTO(
                    p.idProduto,
                    p.nomeProduto,
                    c.idCategoria,
                    c.nomeCategoria,
                    SUM(pp.quantidade) AS qtde,
                    p.preco,
                    SUM(pp.subTotal) AS total
                )
                FROM ProdutoPedido pp
                JOIN pp.produto p
                JOIN p.categoria c
                WHERE pp.data = :data
                GROUP BY p.idProduto, p.nomeProduto, c.idCategoria, c.nomeCategoria
                ORDER BY SUM(pp.subTotal) DESC
            """)
    List<ProdutoDashboardDTO> buscarResumoPorData(@Param("data") LocalDate data);

    List<ProdutoPedido> findByData(LocalDate data);

    @Query("""
    SELECT new com.br.digitalmenu.dto.RelatorioVendasDeProdutoDTO(
        p.nomeProduto,
        SUM(pp.quantidade),
        SUM(pp.subTotal),
        p.categoria.id
    )
    FROM ProdutoPedido pp
    JOIN pp.produto p
    WHERE pp.status = 'FINALIZADO'
    GROUP BY p.id
""")
    List<RelatorioVendasDeProdutoDTO> listarResumoProdutosFinalizados();

    @Query("""
    SELECT new com.br.digitalmenu.dto.RelatorioVendasDeProdutoDTO(
        p.nomeProduto,
        SUM(pp.quantidade),
        SUM(pp.subTotal),
        p.categoria.id
    )
    FROM ProdutoPedido pp
    JOIN pp.produto p
    WHERE pp.status = 'FINALIZADO'
      AND pp.data >= :data
    GROUP BY p.id
""")
    List<RelatorioVendasDeProdutoDTO> listarPorDataIgualOuDepois(@Param("data") LocalDate data);

    @Query("""
    SELECT new com.br.digitalmenu.dto.RelatorioVendasDeProdutoDTO(
        p.nomeProduto,
        SUM(pp.quantidade),
        SUM(pp.subTotal),
        p.categoria.id
    )
    FROM ProdutoPedido pp
    JOIN pp.produto p
    WHERE pp.status = 'FINALIZADO'
      AND pp.data <= :data
    GROUP BY p.id
""")
    List<RelatorioVendasDeProdutoDTO> listarPorDataAntesOuIgual(@Param("data") LocalDate data);

    @Query("""
    SELECT new com.br.digitalmenu.dto.RelatorioVendasDeProdutoDTO(
        p.nomeProduto,
        SUM(pp.quantidade),
        SUM(pp.subTotal),
        p.categoria.id
    )
    FROM ProdutoPedido pp
    JOIN pp.produto p
    WHERE pp.status = 'FINALIZADO'
      AND pp.data BETWEEN :inicio AND :fim
    GROUP BY p.id
""")
    List<RelatorioVendasDeProdutoDTO> listarPorPeriodo(
            @Param("inicio") LocalDate dataInicio,
            @Param("fim") LocalDate dataFim
    );

}
