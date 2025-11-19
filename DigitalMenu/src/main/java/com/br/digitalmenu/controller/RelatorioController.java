package com.br.digitalmenu.controller;

import com.br.digitalmenu.repository.ProdutoPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;

    @GetMapping
    public ResponseEntity<?> teste(){
        return ResponseEntity.ok(produtoPedidoRepository.listarResumoProdutosFinalizados());
    }

}
