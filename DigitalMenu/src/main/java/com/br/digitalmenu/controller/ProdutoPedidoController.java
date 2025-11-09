package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.InsertProdutoPedidoDTO;
import com.br.digitalmenu.dto.response.ProdutoPedidoResponseDTO;
import com.br.digitalmenu.model.ProdutoPedido;
import com.br.digitalmenu.model.StatusPedido;
import com.br.digitalmenu.model.StatusProdutoPedido;
import com.br.digitalmenu.repository.ProdutoPedidoRepository;
import com.br.digitalmenu.repository.ProdutoRepository;
import com.br.digitalmenu.service.ProdutoPedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtoPedido")
@CrossOrigin(origins = "http://localhost:4200")
public class ProdutoPedidoController {

    @Autowired
    private ProdutoPedidoService produtoPedidoService;

    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;

    @PostMapping
    public ResponseEntity<?> insertProdutoPedido(@RequestBody @Valid InsertProdutoPedidoDTO dto){
        return produtoPedidoService.insertProdutoPedido(dto);
    }

    @GetMapping
    public ResponseEntity<?> getProdutosPedidos(
            @RequestParam (required = false) Long idPedido,
            @RequestParam (required = false) Long idMesa,
            @RequestParam (required = false) StatusProdutoPedido statusProdutoPedido,
            @RequestParam (required = false) StatusPedido statusPedido
    ){
        List<ProdutoPedidoResponseDTO> produtoPedidos = produtoPedidoRepository.findAll().stream()
                .filter(produtoPedido -> idPedido == null || produtoPedido.getPedido().getId().equals(idPedido))
                .filter(produtoPedido -> idMesa == null || produtoPedido.getPedido().getMesa().getIdMesa().equals(idPedido))
                .filter(produtoPedido -> statusProdutoPedido == null || produtoPedido.getStatus().equals(statusProdutoPedido))
                .filter(produtoPedido -> statusPedido == null || produtoPedido.getPedido().getStatusPedido().equals(statusPedido)).map(ProdutoPedidoResponseDTO::new).toList();

        return ResponseEntity.ok(produtoPedidos);
    }

    @GetMapping("/byId")
    public ResponseEntity<?> getProdutoPedidoById(@RequestParam Long idProdutoPedido){
        return produtoPedidoRepository.existsById(idProdutoPedido)? ResponseEntity.ok(new ProdutoPedidoResponseDTO(produtoPedidoRepository.getReferenceById(idProdutoPedido))):ResponseEntity.notFound().build();
    }

    @DeleteMapping
    public ResponseEntity<?> cancelarProdutoPedido(@RequestParam Long idProdutoPedido){
        return produtoPedidoService.cancelarProdutoPedido(idProdutoPedido);
    }

    @PutMapping("/finalizarProdutoPedido")
    public ResponseEntity<?> finalizarProdutoPedido(@RequestParam Long idProdutoPedido){
        return produtoPedidoService.finalizarProdutoPedido(idProdutoPedido);
    }
}
