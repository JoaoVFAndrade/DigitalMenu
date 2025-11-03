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
import java.math.RoundingMode;
import java.net.URI;

@Service
public class ProdutoPedidoService {

    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public ResponseEntity<?> insertProdutoPedido(InsertProdutoPedidoDTO dto) {
        ProdutoPedido produtoPedido = new ProdutoPedido();

        produtoPedido.setProduto(produtoRepository.getReferenceById(dto.idProduto()));
        produtoPedido.setPedido(pedidoRepository.getReferenceById(dto.idPedido()));
        produtoPedido.setQuantidade(dto.quantidade());

        // Calcula o subtotal do item
        BigDecimal quantidade = BigDecimal.valueOf(produtoPedido.getQuantidade());
        BigDecimal preco = produtoPedido.getProduto().getPreco();
        BigDecimal subTotal = preco.multiply(quantidade)
                .setScale(2, RoundingMode.HALF_UP);
        produtoPedido.setSubTotal(subTotal);

        // Salva o item
        produtoPedidoRepository.save(produtoPedido);

        // Atualiza o total do pedido automaticamente
        var pedido = produtoPedido.getPedido();
        BigDecimal totalAtual = pedido.getTotal() != null ? pedido.getTotal() : BigDecimal.ZERO;
        BigDecimal novoTotal = totalAtual.add(subTotal).setScale(2, RoundingMode.HALF_UP);
        pedido.setTotal(novoTotal);
        pedidoRepository.save(pedido);

        // Cria o Location do novo recurso
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(produtoPedido.getId())
                .toUri();

        // Retorna o item inserido
        return ResponseEntity.created(location).body(new ProdutoPedidoResponseDTO(produtoPedido));
    }

    public ResponseEntity<?> cancelarProdutoPedido(Long idProdutoPedido) {
        // Verifica se o produto existe
        if (!produtoPedidoRepository.existsById(idProdutoPedido)) {
            return ResponseEntity.notFound().build();
        }

        // Busca o produto pedido
        ProdutoPedido produtoPedido = produtoPedidoRepository.getReferenceById(idProdutoPedido);

        // Verifica se já está cancelado
        if (produtoPedido.getStatus().equals(StatusProdutoPedido.CANCELADO)) {
            return ResponseEntity.status(409).body("Produto já cancelado");
        }

        // Se o produto já foi finalizado, subtrai o subtotal do total do pedido
        if (produtoPedido.getStatus().equals(StatusProdutoPedido.FINALIZADO)) {
            BigDecimal novoTotal = produtoPedido.getPedido()
                    .getTotal()
                    .subtract(produtoPedido.getSubTotal())
                    .setScale(2, RoundingMode.HALF_UP); // garante 2 casas decimais
            produtoPedido.getPedido().setTotal(novoTotal);
        }

        // Atualiza o status do produto para CANCELADO
        produtoPedido.setStatus(StatusProdutoPedido.CANCELADO);

        // Salva as alterações
        produtoPedidoRepository.save(produtoPedido);

        // Retorna a resposta com o DTO atualizado
        return ResponseEntity.ok(new ProdutoPedidoResponseDTO(produtoPedido));
    }

    public ResponseEntity<?> finalizarProdutoPedido(Long idProdutoPedido) {
        // Verifica se o produto existe
        if (!produtoPedidoRepository.existsById(idProdutoPedido)) {
            return ResponseEntity.notFound().build();
        }

        // Busca o produto pedido
        ProdutoPedido produtoPedido = produtoPedidoRepository.getReferenceById(idProdutoPedido);

        // Verifica se o produto ainda está em preparação
        if (!produtoPedido.getStatus().equals(StatusProdutoPedido.EM_PREPARACAO)) {
            return ResponseEntity.status(409)
                    .body("Produto já finalizado ou cancelado");
        }

        // Atualiza o status do produto para FINALIZADO
        produtoPedido.setStatus(StatusProdutoPedido.FINALIZADO);

        // Atualiza o total do pedido somando o subtotal do produto
        BigDecimal novoTotal = produtoPedido.getPedido()
                .getTotal()
                .add(produtoPedido.getSubTotal())
                .setScale(2, RoundingMode.HALF_UP);

        produtoPedido.getPedido().setTotal(novoTotal);

        // Salva as alterações
        produtoPedidoRepository.save(produtoPedido);

        // Retorna a resposta com o DTO do produto pedido finalizado
        return ResponseEntity.ok(new ProdutoPedidoResponseDTO(produtoPedido));
    }
}
