package com.br.digitalmenu.service;

import com.br.digitalmenu.model.Pedido;
import com.br.digitalmenu.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public ResponseEntity<?> getVendasDia() {
        List<Pedido> pedidos = pedidoRepository.findByFinalizadoEmData(LocalDate.now());
        List<Pedido> pedidosOntem = pedidoRepository.findByFinalizadoEmData(LocalDate.now().minusDays(1));

        Optional<BigDecimal> totalDoDia = pedidos.stream().map(Pedido::getTotal).reduce(BigDecimal::add);
        Optional<BigDecimal> totalDoDiaAnterior = pedidosOntem.stream().map(Pedido::getTotal).reduce(BigDecimal::add);

        BigDecimal porcentagem = ((totalDoDia.get().subtract(totalDoDiaAnterior.get()))
                .divide(totalDoDiaAnterior.get(), MathContext.DECIMAL128))
                .multiply(BigDecimal.valueOf(100));

        System.out.println(porcentagem);
        System.out.println(totalDoDia);
        System.out.println(totalDoDiaAnterior);

        return ResponseEntity.ok("teste");
    }
}
