package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.request.IngredienteRequestDTO;
import com.br.digitalmenu.dto.response.IngredienteResponseDTO;
import com.br.digitalmenu.model.Ingrediente;
import com.br.digitalmenu.repository.IngredienteRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredienteService {
    private final IngredienteRepository ingredienteRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository){
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<IngredienteResponseDTO> findAll(){
        return ingredienteRepository.findAll()
                .stream()
                .map(ingrediente -> new IngredienteResponseDTO(ingrediente.getIdIngrediente(), ingrediente.getNomeIngrediente(),ingrediente.getEstoque(),ingrediente.getAtivo()))
                .toList();
    }

    public List<IngredienteResponseDTO> findAllAtivos(){
        return ingredienteRepository.findByAtivoTrue()
                .stream()
                .map(ingrediente -> new IngredienteResponseDTO(ingrediente.getIdIngrediente(), ingrediente.getNomeIngrediente(), ingrediente.getEstoque(), ingrediente.getAtivo()))
                .toList();
    }

    public IngredienteResponseDTO findById(Long id){
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente nao encontrado com id:" + id ));

        return new IngredienteResponseDTO(ingrediente.getIdIngrediente(), ingrediente.getNomeIngrediente(), ingrediente.getEstoque(), ingrediente.getAtivo());
    }

    public IngredienteResponseDTO save (@Valid IngredienteRequestDTO dto){
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNomeIngrediente(dto.getNomeIngrediente());
        ingrediente.setEstoque(dto.getEstoque());
        ingrediente.setAtivo(dto.isAtivo());

        Ingrediente salvar = ingredienteRepository.save(ingrediente);
        return new IngredienteResponseDTO(salvar.getIdIngrediente(), salvar.getNomeIngrediente(), salvar.getEstoque(), salvar.getAtivo());
    }

    public void delete(Long id){
        ingredienteRepository.findById(id).map(ingrediente -> {ingrediente.setAtivo(false);
        return ingredienteRepository.save(ingrediente);
        }).orElseThrow(() -> new RuntimeException("Ingrediente nao encontrado com id "+ id));
    }

    public void ativaIngrediente(Long id){
        ingredienteRepository.findById(id).map(ingrediente -> {ingrediente.setAtivo(true);
        return ingredienteRepository.save(ingrediente);
        }).orElseThrow(() -> new RuntimeException("Ingrediente nao encontrado com id " + id));
    }

    public IngredienteResponseDTO atualizaIngrediente(Long id, IngredienteRequestDTO dto){
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente nao encontrado com id "+ id));
        if (dto.getNomeIngrediente() != null) {
            ingrediente.setNomeIngrediente(dto.getNomeIngrediente());
        }

        if (dto.getEstoque() != null) {
            ingrediente.setEstoque(dto.getEstoque());
        }

        Ingrediente atualizado = ingredienteRepository.save(ingrediente);

        return new IngredienteResponseDTO(
                atualizado.getIdIngrediente(),
                atualizado.getNomeIngrediente(),
                atualizado.getEstoque(),
                atualizado.getAtivo()
        );
    }
}

