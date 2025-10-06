package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.InsertProdutoPedidoDTO;
import com.br.digitalmenu.dto.response.ProdutoPedidoResponseDTO;
import com.br.digitalmenu.model.ProdutoPedido;
import com.br.digitalmenu.model.StatusProdutoPedido;
import com.br.digitalmenu.repository.PedidoRepository;
import com.br.digitalmenu.repository.ProdutoPedidoRepository;
import com.br.digitalmenu.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;

@Service
public class ProdutoPedidoService {

    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public ResponseEntity<?> insertProdutoPedido(InsertProdutoPedidoDTO dto){
        ProdutoPedido produtoPedido = new ProdutoPedido();

        produtoPedido.setProduto(produtoRepository.getReferenceById(dto.idProduto()));
        produtoPedido.setPedido(pedidoRepository.getReferenceById(dto.idPedido()));
        produtoPedido.setQuantidade(dto.quantidade());
        produtoPedido.setSubTotal(Math.round(produtoPedido.getQuantidade() * produtoPedido.getProduto().getPreco() * 100.0) / 100.0);

        produtoPedidoRepository.save(produtoPedido);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build(produtoPedido);

        return ResponseEntity.created(location).body(produtoPedido);
    }

    public ResponseEntity<?> cancelarProdutoPedido(Long idProdutoPedido){
        if(!produtoPedidoRepository.existsById(idProdutoPedido))
            return ResponseEntity.notFound().build();

        ProdutoPedido produtoPedido = produtoPedidoRepository.getReferenceById(idProdutoPedido);

        if(produtoPedido.getStatus().equals(StatusProdutoPedido.CANCELADO))
            return ResponseEntity.status(409).body("Produto já cancelado");

        if(produtoPedido.getStatus().equals(StatusProdutoPedido.FINALIZADO))
            produtoPedido.getPedido().setTotal(produtoPedido.getPedido().getTotal().subtract(BigDecimal.valueOf(produtoPedido.getSubTotal())));

        produtoPedido.setStatus(StatusProdutoPedido.CANCELADO);

        produtoPedidoRepository.save(produtoPedido);

        return ResponseEntity.ok(new ProdutoPedidoResponseDTO(produtoPedido));
    }

    public ResponseEntity<?> finalizarProdutoPedido(Long idProdutoPedido){
        if(!produtoPedidoRepository.existsById(idProdutoPedido))
            return ResponseEntity.notFound().build();

        ProdutoPedido produtoPedido = produtoPedidoRepository.getReferenceById(idProdutoPedido);

        if(!produtoPedido.getStatus().equals(StatusProdutoPedido.EM_PREPARACAO))
            return ResponseEntity.status(409).body("Produto já finalizado ou cancelado");

        produtoPedido.setStatus(StatusProdutoPedido.FINALIZADO);
        produtoPedido.getPedido().setTotal(produtoPedido.getPedido().getTotal().add(BigDecimal.valueOf(produtoPedido.getSubTotal())));

        produtoPedidoRepository.save(produtoPedido);

        return ResponseEntity.ok(new ProdutoPedidoResponseDTO(produtoPedido));
    }
}
