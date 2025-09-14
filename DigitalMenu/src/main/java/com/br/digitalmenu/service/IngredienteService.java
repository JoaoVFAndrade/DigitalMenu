package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.request.IngredienteRequestDTO;
import com.br.digitalmenu.dto.response.IngredienteResponseDTO;
import com.br.digitalmenu.model.Ingrediente;
import com.br.digitalmenu.model.Restricao;
import com.br.digitalmenu.repository.IngredienteRepository;
import com.br.digitalmenu.repository.RestricaoRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredienteService {
    private final IngredienteRepository ingredienteRepository;
    private final RestricaoRepository restricaoRepository;

    public IngredienteService(IngredienteRepository ingredienteRepository,RestricaoRepository restricaoRepository){
        this.ingredienteRepository = ingredienteRepository;
        this.restricaoRepository = restricaoRepository;
    }

    public List<IngredienteResponseDTO> findAll(){
        return ingredienteRepository.findAll()
                .stream()
                .map(IngredienteResponseDTO::new)
                .toList();
    }

    public List<IngredienteResponseDTO> findAllAtivos(){
        return ingredienteRepository.findByAtivoTrue()
                .stream()
                .map(IngredienteResponseDTO::new)
                .toList();
    }

    public IngredienteResponseDTO findById(Long id){
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente nao encontrado com idMesa:" + id ));

        return new IngredienteResponseDTO(ingrediente);
    }

    public IngredienteResponseDTO save (@Valid IngredienteRequestDTO dto){
        Ingrediente ingrediente = new Ingrediente();
        ingrediente.setNomeIngrediente(dto.getNomeIngrediente());
        ingrediente.setRestricoes(restricaoRepository.findAllById(dto.getIdRestricoes()));
        ingrediente.setEstoque(dto.getEstoque());
        ingrediente.setAtivo(dto.isAtivo());

        Ingrediente salvar = ingredienteRepository.save(ingrediente);
        return new IngredienteResponseDTO(ingrediente);
    }

    public void delete(Long id){
        ingredienteRepository.findById(id).map(ingrediente -> {ingrediente.setAtivo(false);
        return ingredienteRepository.save(ingrediente);
        }).orElseThrow(() -> new RuntimeException("Ingrediente nao encontrado com idMesa "+ id));
    }

    public void ativaIngrediente(Long id){
        ingredienteRepository.findById(id).map(ingrediente -> {ingrediente.setAtivo(true);
        return ingredienteRepository.save(ingrediente);
        }).orElseThrow(() -> new RuntimeException("Ingrediente nao encontrado com idMesa " + id));
    }

    public IngredienteResponseDTO atualizaIngrediente(Long id, IngredienteRequestDTO dto){
        Ingrediente ingrediente = ingredienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ingrediente nao encontrado com idMesa "+ id));
        if (dto.getNomeIngrediente() != null) {
            ingrediente.setNomeIngrediente(dto.getNomeIngrediente());
        }

        if (dto.getEstoque() != null) {
            ingrediente.setEstoque(dto.getEstoque());
        }

        if(dto.getIdRestricoes() != null){
            ingrediente.setRestricoes(restricaoRepository.findAllById(dto.getIdRestricoes()));
        }

        Ingrediente atualizado = ingredienteRepository.save(ingrediente);

        return new IngredienteResponseDTO(ingrediente);
    }
}

