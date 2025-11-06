package com.br.digitalmenu.controller;

import com.br.digitalmenu.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/vendasDoDia")
    public ResponseEntity<?> getVendasDoDia(){
        return dashboardService.getVendasDia();
    }

    @GetMapping("/qtdePedidos")
    public ResponseEntity<?> getQuantidadeDePedidos(){
        return dashboardService.getQuantidadePedidos();
    }

    @GetMapping("/qtdeProdutos")
    public ResponseEntity<?> getQuantidadeDeProdutos(){
        return dashboardService.getQuantidadeProdutos();
    }

    @GetMapping("/top10ProdutosDoDia")
    public ResponseEntity<?> getTop10ProdutosDoDia(){
        return dashboardService.getTop10ProdutosMaisVendidosDoDia();
    }

}
