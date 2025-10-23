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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        cliente.setSenha(passwordEncoder.encode(dto.getSenha()));
        cliente.setDataNascimento(dto.getDataNascimento());
        cliente.setEmailValidado(false);

        Cliente salvo = clienteRepository.save(cliente);

        try {
            String codigo = verificacaoService.gerarCodigo(cliente.getEmail());
            emailService.enviarCodigoVerificacao(cliente.getEmail(), codigo);
        }catch (MessagingException e){
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

    public ResponseEntity<?> insertRestricoesCliente(RestricoesClienteRequestDTO dto){
        Cliente cliente = clienteRepository.getReferenceById(dto.idCliente());

        cliente.setRestricoes(restricaoRepository.findAllById(dto.idRestricoes()));

        clienteRepository.save(cliente);

        return ResponseEntity.ok("Restriçoes adicionadas com sucesso");
    }

    public ResponseEntity<?> deleteRestricoesCliente(RestricoesClienteRequestDTO dto){
        Cliente cliente = clienteRepository.getReferenceById(dto.idCliente());

        List<Restricao> restricoes = restricaoRepository.findAllById(dto.idRestricoes());

        restricoes.forEach(restricao -> {
            cliente.getRestricoes().remove(restricao);
        });

        clienteRepository.save(cliente);

        return ResponseEntity.ok("Restricoes deletadas com sucesso");
    }

    public void deletar(Long id) {
        clienteRepository.deleteById(id);
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
