package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.request.PedidoRequestDTO;
import com.br.digitalmenu.dto.response.PedidoResponseDTO;
import com.br.digitalmenu.model.Pedido;
import com.br.digitalmenu.model.StatusPedido;
import com.br.digitalmenu.repository.PedidoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    public List<PedidoResponseDTO> findAll(){
        return pedidoRepository.findAll()
                .stream()
                .map(pedido -> new PedidoResponseDTO(pedido.getId(), pedido.getAbertoEm(), pedido.getFinalizadoEm(),
                        pedido.getStatusPedido(), pedido.getTotal())).toList();
    }

    public List<PedidoResponseDTO> findAllAberto(){
        return pedidoRepository.findByStatusPedido(StatusPedido.ABERTO)
                .stream()
                .map(pedido -> new PedidoResponseDTO(pedido.getId(), pedido.getAbertoEm(), pedido.getFinalizadoEm(),
                        pedido.getStatusPedido(), pedido.getTotal())).toList();
    }

    public List<PedidoResponseDTO> findAllFinalizado(){
        return pedidoRepository.findByStatusPedido(StatusPedido.FINALIZADO)
                .stream()
                .map(pedido -> new PedidoResponseDTO(pedido.getId(), pedido.getAbertoEm(), pedido.getFinalizadoEm(),
                        pedido.getStatusPedido(), pedido.getTotal())).toList();
    }

    public PedidoResponseDTO findById(Long id){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado"));

        return new PedidoResponseDTO(pedido.getId(), pedido.getAbertoEm(), pedido.getFinalizadoEm(),
                pedido.getStatusPedido(), pedido.getTotal());
    }

    public PedidoResponseDTO save (@Valid PedidoRequestDTO dto){
        Pedido pedido = new Pedido();
        pedido.setAbertoEm(LocalDateTime.now());
        pedido.setFinalizadoEm(dto.getFinalizadoEm());
        pedido.setStatusPedido(StatusPedido.ABERTO);
        pedido.setTotal(dto.getTotal());

        Pedido salvar = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(salvar.getId(),salvar.getAbertoEm(),salvar.getFinalizadoEm(),
                salvar.getStatusPedido(), salvar.getTotal());
    }

    public void delete(Long id){
        pedidoRepository.findById(id).map(pedido -> {pedido.setStatusPedido(StatusPedido.CANCELADO);
        return pedidoRepository.save(pedido);
        }).orElseThrow(() -> new RuntimeException("pedido nao encontrado"));
    }

    public PedidoResponseDTO atualizaPedido(Long id, PedidoRequestDTO dto){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("pedido nao encontrado"));

        if (dto.getTotal() != null) {
            pedido.setTotal(dto.getTotal());
        }

        if (dto.getStatusPedido() != null) {
            pedido.setStatusPedido(dto.getStatusPedido());

            if (dto.getStatusPedido() == StatusPedido.FINALIZADO) {
                pedido.setFinalizadoEm(
                        dto.getFinalizadoEm() != null ? dto.getFinalizadoEm() : LocalDateTime.now()
                );
            }
        }

        Pedido atualizado = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(
                atualizado.getId(),
                atualizado.getAbertoEm(),
                atualizado.getFinalizadoEm(),
                atualizado.getStatusPedido(),
                atualizado.getTotal()
        );
    }
}
