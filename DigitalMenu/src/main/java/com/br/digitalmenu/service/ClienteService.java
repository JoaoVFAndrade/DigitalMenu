package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.RestricaoDTO;
import com.br.digitalmenu.dto.request.ClienteUpdateDTO;
import com.br.digitalmenu.dto.request.RestricoesClienteRequestDTO;
import com.br.digitalmenu.dto.request.ClienteRequestDTO;
import com.br.digitalmenu.dto.response.ClienteResponseDTO;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.model.Restricao;
import com.br.digitalmenu.repository.ClienteRepository;
import com.br.digitalmenu.repository.RestricaoRepository;
import jakarta.mail.MessagingException;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificacaoService verificacaoService;

    @Autowired
    private RestricaoRepository restricaoRepository;

    @Autowired
    private EmailService emailService;

    public ClienteResponseDTO salvar(ClienteRequestDTO dto) throws MessagingException {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setRestricoes(new ArrayList<>());
        cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setEmailValidado(false);

        Cliente salvo = clienteRepository.save(cliente);

        try {
            String codigo = verificacaoService.gerarCodigo(cliente.getEmail());
            emailService.enviarCodigoVerificacao(cliente.getEmail(), codigo);
        } catch (MessagingException e) {
            throw new RuntimeException("Erro ao enviar e-mail", e);
        }

        return toResponseDTO(salvo);
    }

    public List<ClienteResponseDTO> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return toResponseDTO(cliente);
    }

    public ClienteResponseDTO atualizar(Long id, ClienteUpdateDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (dto.getNome() != null) cliente.setNome(dto.getNome());
        if (dto.getEmail() != null) cliente.setEmail(dto.getEmail());
        if (dto.getDataNascimento() != null) cliente.setDataNascimento(dto.getDataNascimento());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        Cliente atualizado = clienteRepository.save(cliente);
        return toResponseDTO(atualizado);
    }

    public ResponseEntity<?> atualizarRestricoesCliente(RestricoesClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (dto.getRestricoesParaAdicionar() != null && !dto.getRestricoesParaAdicionar().isEmpty()) {
            List<Restricao> novas = restricaoRepository.findAllById(dto.getRestricoesParaAdicionar());
            cliente.getRestricoes().addAll(novas);
        }

        if (dto.getRestricoesParaRemover() != null && !dto.getRestricoesParaRemover().isEmpty()) {
            List<Restricao> remover = restricaoRepository.findAllById(dto.getRestricoesParaRemover());
            cliente.getRestricoes().removeAll(remover);
        }

        clienteRepository.save(cliente);
        return ResponseEntity.ok("Restrições atualizadas com sucesso");
    }

    public void deletar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome("ANON_" + UUID.randomUUID().toString().substring(0, 8));
        cliente.setEmail("anon_" + cliente.getIdCliente() + "_" + UUID.randomUUID().toString().substring(0, 5) + "@example.com");
        cliente.setSenha(passwordEncoder.encode(UUID.randomUUID().toString().substring(0, 12)));
        cliente.setDataNascimento(LocalDate.of(2000, 1, 1));

        clienteRepository.save(cliente); // @TODO adicionar um campo na tabela cliente para dizer que foi excluido;
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        ClienteResponseDTO dto = new ClienteResponseDTO();
        dto.setId(cliente.getIdCliente());
        dto.setNome(cliente.getNome());
        dto.setEmail(cliente.getEmail());
        dto.setDataNascimento(cliente.getDataNascimento());
        dto.setRestricoes(cliente.getRestricoes().stream().map(restricao -> new RestricaoDTO(restricao.getIdRestricao(), restricao.getNomeRestricao(), restricao.getTipoRestricao())).toList());
        return dto;
    }

}
