package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.request.ClienteRequestDTO;
import com.br.digitalmenu.dto.response.ClienteResponseDTO;
import com.br.digitalmenu.model.Cliente;
import com.br.digitalmenu.repository.ClienteRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome(dto.getNome());
        cliente.setEmail(dto.getEmail());
        cliente.setSenha(dto.getSenha());
        cliente.setDataNascimento(dto.getDataNascimento());

        Cliente atualizado = clienteRepository.save(cliente);
        return toResponseDTO(atualizado);
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
        return dto;
    }

}
