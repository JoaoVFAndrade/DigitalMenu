package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.PedidoRequestDTO;
import com.br.digitalmenu.dto.response.PedidoResponseDTO;
import com.br.digitalmenu.service.PagamentoService;
import com.br.digitalmenu.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PagamentoService pagamentoService;

    @GetMapping("/all")
    public List<PedidoResponseDTO> getAllPedidos(){return pedidoService.findAll();}

    @GetMapping("/abertos")
    public List<PedidoResponseDTO> getAllAberto(){return pedidoService.findAllAberto();}

    @GetMapping("/finalizados")
    public List<PedidoResponseDTO> getAllFinalizado(){return pedidoService.findAllFinalizado();}

    @GetMapping("/{idMesa}")
    public ResponseEntity<PedidoResponseDTO> getById(@PathVariable Long id){
        try {
            return ResponseEntity.ok(pedidoService.findById(id));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> create(@RequestBody @Valid PedidoRequestDTO requestDTO){
        PedidoResponseDTO responseDTO = pedidoService.save(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @DeleteMapping("/{idMesa}")
    public ResponseEntity<Void> cancelaPedido(@PathVariable Long id){
        try {
            pedidoService.delete(id);
            return ResponseEntity.noContent().build();
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{idMesa}")
    public ResponseEntity<PedidoResponseDTO> atualizaPedido(@PathVariable Long id, @RequestBody @Valid PedidoRequestDTO pedidoRequestDTO){
        try {
            return ResponseEntity.ok(pedidoService.atualizaPedido(id, pedidoRequestDTO));
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/gerarPagamento")
    public ResponseEntity<?> realizarPagamento(@RequestParam Long id){
        return pagamentoService.gerarPagamento(id);
    }
}
