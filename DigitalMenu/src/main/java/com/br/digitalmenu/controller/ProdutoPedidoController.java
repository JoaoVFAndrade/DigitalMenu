package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.InsertProdutoPedidoDTO;
import com.br.digitalmenu.service.ProdutoPedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtoPedido")
public class ProdutoPedidoController {

    @Autowired
    private ProdutoPedidoService produtoPedidoService;

    @PostMapping
    public ResponseEntity<?> insertProdutoPedido(@RequestBody @Valid InsertProdutoPedidoDTO dto){
        return produtoPedidoService.insertProdutoPedido(dto);
    }
}
