package com.br.digitalmenu.dto;

import java.math.BigDecimal;



public class ProdutoDashboardDTO {
    private Long idProduto;
    private String nomeProduto;
    private Long idCategoria;
    private String nomeCategoria;
    private Long quantidade;
    private BigDecimal preco;
    private BigDecimal total;

    public ProdutoDashboardDTO(Long idProduto, String nomeProduto,
                               Long idCategoria, String nomeCategoria,
                               Long quantidade, BigDecimal preco, BigDecimal total) {
        this.idProduto = idProduto;
        this.nomeProduto = nomeProduto;
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
        this.quantidade = quantidade;
        this.preco = preco;
        this.total = total;
    }

    public Long getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(Long idProduto) {
        this.idProduto = idProduto;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public Long getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Long quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
