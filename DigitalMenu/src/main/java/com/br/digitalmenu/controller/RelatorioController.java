package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.RelatorioFinalDTO;
import com.br.digitalmenu.dto.RelatorioVendasDeProdutoDTO;
import com.br.digitalmenu.repository.ProdutoPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;

    @GetMapping
    public ResponseEntity<?> getRelatorios(
            @RequestParam (required = false) Long idCategoria,
            @RequestParam (required = false) LocalDate dataInicial,
            @RequestParam (required = false) LocalDate dataFinal
            ){

        List<RelatorioVendasDeProdutoDTO> listRelatoriosVenda = new ArrayList<>();

        if(dataFinal == null && dataInicial == null){
            listRelatoriosVenda.addAll(produtoPedidoRepository.listarResumoProdutosFinalizados());
        } else if (dataInicial != null) {
            listRelatoriosVenda.addAll(produtoPedidoRepository.listarPorDataIgualOuDepois(dataInicial));
        }else{
            listRelatoriosVenda.addAll(produtoPedidoRepository.listarPorDataAntesOuIgual(dataFinal));
        }

        List<RelatorioVendasDeProdutoDTO> listRelatoriosVendaFiltrado = listRelatoriosVenda.stream().filter(relatorioVendasDeProdutoDTO -> idCategoria == null || relatorioVendasDeProdutoDTO.getIdCategoria().equals(idCategoria)).toList();

        RelatorioFinalDTO relatorioFinalDTO = new RelatorioFinalDTO(
                listRelatoriosVendaFiltrado,
                listRelatoriosVendaFiltrado.stream()
                        .map(RelatorioVendasDeProdutoDTO::getTotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add),
                listRelatoriosVendaFiltrado.stream()
                        .map(RelatorioVendasDeProdutoDTO::getQtde)
                        .reduce(0L, Long::sum));

        return ResponseEntity.ok(relatorioFinalDTO);
    }









}
