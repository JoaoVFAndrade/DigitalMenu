package com.br.digitalmenu.controller;

import com.br.digitalmenu.dto.request.ClienteRequestDTO;
import com.br.digitalmenu.dto.response.ClienteResponseDTO;
import com.br.digitalmenu.service.ClienteService;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.br.digitalmenu.dto.request.RestricoesClienteRequestDTO;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping("/cadastro")
    public ResponseEntity<ClienteResponseDTO> criar(@Valid @RequestBody ClienteRequestDTO dto) throws MessagingException {
        ClienteResponseDTO responseDTO = clienteService.salvar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> listar() {
        return ResponseEntity.ok(clienteService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(clienteService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(@PathVariable Long id, @Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(clienteService.atualizar(id, dto));
    }

    @PutMapping("/insertRestricoes")
    public ResponseEntity<?> inserirRestricoesCliente(@RequestBody @Valid RestricoesClienteRequestDTO dto){
        return clienteService.insertRestricoesCliente(dto);
    }

    @PutMapping("/deleteRestricoesCliente")
    public ResponseEntity<?> deleteRestricoesCliente(@RequestBody @Valid RestricoesClienteRequestDTO dto){
        return clienteService.deleteRestricoesCliente(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        clienteService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
