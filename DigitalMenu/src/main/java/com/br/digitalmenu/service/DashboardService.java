package com.br.digitalmenu.service;

import com.br.digitalmenu.model.Pedido;
import com.br.digitalmenu.model.ProdutoPedido;
import com.br.digitalmenu.repository.PedidoRepository;
import com.br.digitalmenu.repository.ProdutoPedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DashboardService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;

    public ResponseEntity<?> getVendasDia() {
        List<Pedido> pedidos = pedidoRepository.findByFinalizadoEmData(LocalDate.now());
        List<Pedido> pedidosOntem = pedidoRepository.findByFinalizadoEmData(LocalDate.now().minusDays(1));

        BigDecimal totalDoDia = pedidos.stream()
                .map(Pedido::getTotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        BigDecimal totalDoDiaAnterior = pedidosOntem.stream()
                .map(Pedido::getTotal)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);

        if (totalDoDiaAnterior.compareTo(BigDecimal.ZERO) == 0) {
            return ResponseEntity.ok(Map.of(
                    "vendasHoje", totalDoDia,
                    "porcentagem", "+∞"
            ));
        }

        if (totalDoDia.compareTo(BigDecimal.ZERO) == 0) {
            return ResponseEntity.ok(Map.of(
                    "vendasHoje", 0,
                    "porcentagem", "-∞"
            ));
        }

        BigDecimal porcentagem = totalDoDia.subtract(totalDoDiaAnterior)
                .divide(totalDoDiaAnterior, MathContext.DECIMAL128)
                .multiply(BigDecimal.valueOf(100));

        return ResponseEntity.ok(Map.of(
                "vendasHoje", totalDoDia,
                "porcentagem", porcentagem
        ));
    }

    public ResponseEntity<?> getQuantidadePedidos(){
        List<Pedido> pedidosHoje = pedidoRepository.findByFinalizadoEmData(LocalDate.now());
        List<Pedido> pedidosOntem = pedidoRepository.findByFinalizadoEmData(LocalDate.now().minusDays(1));

        if(pedidosOntem.isEmpty()){
            return ResponseEntity.ok(Map.of("totalPedidos",pedidosHoje.size() ,"porcentagem", "+∞"));
        }

        double porcentagem = (double) ((pedidosHoje.size() - pedidosOntem.size()) / pedidosOntem.size()) * 100;

        return ResponseEntity.ok(Map.of("totalPedidos",pedidosHoje.size() ,"porcentagem",porcentagem));
    }

    public ResponseEntity<?> getQuantidadeProdutos(){
        List<ProdutoPedido> produtoPedidos = produtoPedidoRepository.findByData(LocalDate.now());
        List<ProdutoPedido> produtoPedidosOntem = produtoPedidoRepository.findByData(LocalDate.now().minusDays(1));

        Optional<Integer> totalProdutosHoje = produtoPedidos.stream().map(ProdutoPedido::getQuantidade).reduce(Integer::sum);
        Optional<Integer> totalProdutosOntem = produtoPedidosOntem.stream().map(ProdutoPedido::getQuantidade).reduce(Integer::sum);

        if(totalProdutosOntem.isEmpty()){
            return ResponseEntity.ok(Map.of("totalPedidos",totalProdutosHoje.get() ,"porcentagem", "+∞"));
        }

        double porcentagem = (double) ((totalProdutosHoje.get() - totalProdutosOntem.get()) / totalProdutosOntem.get()) * 100;

        return ResponseEntity.ok(Map.of("totalPedidos",totalProdutosHoje.get() ,"porcentagem",porcentagem));
    }

    public ResponseEntity<?> getTop10ProdutosMaisVendidosDoDia(){
        return ResponseEntity.ok(produtoPedidoRepository.buscarResumoPorData(LocalDate.now()));
    }

}
