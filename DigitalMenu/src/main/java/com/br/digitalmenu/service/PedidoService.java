package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.request.PedidoRequestDTO;
import com.br.digitalmenu.dto.response.ClienteResponseDTO;
import com.br.digitalmenu.dto.response.MesaResponseDTO;
import com.br.digitalmenu.dto.response.PedidoResponseDTO;
import com.br.digitalmenu.dto.response.ProdutoPedidoResponseDTO;
import com.br.digitalmenu.exception.ResourceNotFoundException;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.model.Mesa;
import com.br.digitalmenu.model.Pedido;
import com.br.digitalmenu.model.StatusPedido;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.repository.MesaRepository;
import com.br.digitalmenu.repository.PedidoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdutoPedidoService produtoPedidoService;

    public List<PedidoResponseDTO> findAll(){
        return pedidoRepository.findAll()
                .stream()
                .map(pedido -> new PedidoResponseDTO(pedido.getId(), pedido.getAbertoEm(), pedido.getFinalizadoEm(),
                        pedido.getStatusPedido(), pedido.getTotal(),new ClienteResponseDTO(pedido.getCliente()) ,new MesaResponseDTO(pedido.getMesa()),pedido.getProdutoPedidos().stream().map(ProdutoPedidoResponseDTO::new).toList())).toList();
    }

    public List<PedidoResponseDTO> findAllAberto(){
        return pedidoRepository.findByStatusPedido(StatusPedido.ABERTO)
                .stream()
                .map(pedido -> new PedidoResponseDTO(pedido.getId(), pedido.getAbertoEm(), pedido.getFinalizadoEm(),
                        pedido.getStatusPedido(), pedido.getTotal(),new ClienteResponseDTO(pedido.getCliente()) ,new MesaResponseDTO(pedido.getMesa()),pedido.getProdutoPedidos().stream().map(ProdutoPedidoResponseDTO::new).toList())).toList();
    }

    public List<PedidoResponseDTO> findAllFinalizado(){
        return pedidoRepository.findByStatusPedido(StatusPedido.FINALIZADO)
                .stream()
                .map(pedido -> new PedidoResponseDTO(pedido.getId(), pedido.getAbertoEm(), pedido.getFinalizadoEm(),
                        pedido.getStatusPedido(), pedido.getTotal(),new ClienteResponseDTO(pedido.getCliente()) ,new MesaResponseDTO(pedido.getMesa()),pedido.getProdutoPedidos().stream().map(ProdutoPedidoResponseDTO::new).toList())).toList();
    }

    public PedidoResponseDTO findById(Long id){
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido nao encontrado"));

        return new PedidoResponseDTO(pedido.getId(), pedido.getAbertoEm(), pedido.getFinalizadoEm(),
                pedido.getStatusPedido(), pedido.getTotal(),new ClienteResponseDTO(pedido.getCliente()) ,new MesaResponseDTO(pedido.getMesa()),pedido.getProdutoPedidos().stream().map(ProdutoPedidoResponseDTO::new).toList());
    }

    public PedidoResponseDTO save (@Valid PedidoRequestDTO dto){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        boolean isGarcom = roles.stream()
                .anyMatch(r -> r.getAuthority().equals("FUNCIONARIO_GARCOM"));

        Mesa mesa = mesaRepository.findById(dto.getIdMesa())
                .orElseThrow(() -> new ResourceNotFoundException("Mesa com ID " + dto.getIdMesa() + " não encontrada."));

        Pedido pedido = new Pedido();
        pedido.setAbertoEm(LocalDateTime.now());
        pedido.setStatusPedido(StatusPedido.ABERTO);
        pedido.setTotal(BigDecimal.ZERO);
        pedido.setProdutoPedidos(new ArrayList<>());

        if (isGarcom) {
            Cliente clientePadraoGarcom = clienteRepository.findByEmail("email@email.com")
                    .orElseThrow(() -> new EntityNotFoundException("Cliente padrão 'email@email.com' não encontrado!"));
            pedido.setCliente(clientePadraoGarcom);
        } else {
            pedido.setCliente(clienteRepository.getReferenceById(dto.getIdCliente()));
        }
        pedido.setMesa(mesa);

        Pedido salvar = pedidoRepository.save(pedido);
        return new PedidoResponseDTO(salvar.getId(),salvar.getAbertoEm(),salvar.getFinalizadoEm(),
                salvar.getStatusPedido(), salvar.getTotal(),new ClienteResponseDTO(salvar.getCliente()),new MesaResponseDTO(salvar.getMesa()),pedido.getProdutoPedidos().stream().map(ProdutoPedidoResponseDTO::new).toList());
    }

    public void delete(Long id) {
        pedidoRepository.findById(id).map(pedido -> {
            pedido.setStatusPedido(StatusPedido.CANCELADO);
            pedido.getProdutoPedidos().forEach(produtoPedido -> {
                produtoPedidoService.cancelarProdutoPedido(produtoPedido.getId());
            });
            pedido.setFinalizadoEm(LocalDateTime.now());
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
                atualizado.getTotal(),
                new ClienteResponseDTO(atualizado.getCliente()),
                new MesaResponseDTO(atualizado.getMesa()),
                pedido.getProdutoPedidos().stream().map(ProdutoPedidoResponseDTO::new).toList()
        );
    }

        public void atualizarStatusPagamento(Long pedidoId, String statusPagBank) {
            Pedido pedido = pedidoRepository.findById(pedidoId)
                    .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

            switch (statusPagBank.toUpperCase()) {
                case "PAID":
                case "PAID_OUT":
                    pedido.setStatusPedido(StatusPedido.FINALIZADO);
                    pedido.setFinalizadoEm(LocalDateTime.now());
                    break;

                case "DECLINED":
                case "CANCELLED":
                    pedido.setStatusPedido(StatusPedido.CANCELADO);
                    pedido.setFinalizadoEm(LocalDateTime.now());
                    break;

                case "WAITING":
                case "IN_ANALYSIS":
                    pedido.setStatusPedido(StatusPedido.ABERTO);
                    break;

                default:
                    System.out.println("Status desconhecido recebido do PagBank: " + statusPagBank);
                    break;
            }

            pedidoRepository.save(pedido);
        }
    }

