package com.br.digitalmenu.service;


import com.br.digitalmenu.dto.request.FuncionarioRequestDTO;
import com.br.digitalmenu.dto.response.FuncionarioResponseDTO;
import com.br.digitalmenu.exception.ResourceNotFoundException;
import com.br.digitalmenu.model.Funcionario;
import com.br.digitalmenu.model.RoleNome;
import com.br.digitalmenu.repository.FuncionarioRepository;
import com.br.digitalmenu.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public FuncionarioResponseDTO criarFuncionario(FuncionarioRequestDTO dto) {

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        if(Util.validarSenha(dto.getSenha())){
            funcionario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }else{
            throw new RuntimeException("Senha no padrão inválido");
        }

        if (dto.getRoles() != null) {
            var roles = dto.getRoles().stream()
                    .map(RoleNome::valueOf)
                    .collect(Collectors.toSet());
            funcionario.setRoles(roles);
        }

        funcionario = funcionarioRepository.save(funcionario);
        return mapToDTO(funcionario);
    }

    public List<FuncionarioResponseDTO> listarFuncionarios() {
        return funcionarioRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FuncionarioResponseDTO buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Funcionario nao encontrado"));
    }

    public FuncionarioResponseDTO buscarPorEmail(String email) {
        Funcionario funcionario = funcionarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Funcionario nao encontrado com email: " + email));
        return mapToDTO(funcionario);
    }

    public FuncionarioResponseDTO atualizarFuncionario(Long id, FuncionarioRequestDTO dto) {
        Funcionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funcionario nao encontrado"));

        funcionario.setNome(dto.getNome());
        funcionario.setEmail(dto.getEmail());
        funcionario.setSenha(dto.getSenha());

        if (dto.getRoles() != null) {
            var roles = dto.getRoles().stream()
                    .map(RoleNome::valueOf)
                    .collect(Collectors.toSet());
            funcionario.setRoles(roles);
        }

        funcionario = funcionarioRepository.save(funcionario);
        return mapToDTO(funcionario);
    }

    public void deletarFuncionario(Long id) {
        funcionarioRepository.deleteById(id);
    }

    private FuncionarioResponseDTO mapToDTO(Funcionario f) {
        FuncionarioResponseDTO dto = new FuncionarioResponseDTO();
        dto.setIdFuncionario(f.getIdFuncionario());
        dto.setNome(f.getNome());
        dto.setEmail(f.getEmail());
        dto.setRoles(f.getRoles()
                .stream()
                .map(Enum::name)
                .collect(Collectors.toSet()));
        return dto;
    }

}
