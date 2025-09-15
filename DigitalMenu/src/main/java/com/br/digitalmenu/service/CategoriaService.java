package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.request.CategoriaRequestDTO;
import com.br.digitalmenu.dto.response.CategoriaResponseDTO;
import com.br.digitalmenu.model.Categoria;
import com.br.digitalmenu.repository.CategoriaRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository){
        this.categoriaRepository = categoriaRepository;
    }

    public List<CategoriaResponseDTO> findAll(){
        return categoriaRepository.findAll()
                .stream()
                .map(categoria -> new CategoriaResponseDTO(categoria.getIdCategoria(),categoria.getNomeCategoria(), categoria.getAtivo()))
                .toList();
    }

    public List<CategoriaResponseDTO> findAllAtivos(){
        return categoriaRepository.findByAtivoTrue()
                .stream()
                .map(categoria -> new CategoriaResponseDTO(categoria.getIdCategoria(), categoria.getNomeCategoria(), categoria.getAtivo()))
                .toList();
    }

    public CategoriaResponseDTO findById(Long id) {
       Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com id " + id));

        return new CategoriaResponseDTO(categoria.getIdCategoria(), categoria.getNomeCategoria(), categoria.getAtivo());
    }

    public CategoriaResponseDTO save(@Valid CategoriaRequestDTO dto){
        Categoria categoria = new Categoria();
        categoria.setNomeCategoria(dto.getNomeCategoria());
        categoria.setAtivo(dto.isAtivo());

        Categoria salvar = categoriaRepository.save(categoria);
        return new CategoriaResponseDTO(salvar.getIdCategoria(), salvar.getNomeCategoria(), salvar.getAtivo());
    }

    public void delete(Long id){
        categoriaRepository.findById(id).map(categoria -> {
            categoria.setAtivo(false);
            return categoriaRepository.save(categoria);
        }).orElseThrow(() -> new RuntimeException("Categoria nao encontrada com id " + id));
    }

    public void ativaCategoria(Long id){
        categoriaRepository.findById(id).map(categoria -> {
            categoria.setAtivo(true);
            return categoriaRepository.save(categoria);
        }).orElseThrow(() -> new RuntimeException("Categoria nao encontrada com id " + id));
    }

    public CategoriaResponseDTO atualizaCategoria(Long id, CategoriaRequestDTO dto) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com id " + id));

        categoria.setNomeCategoria(dto.getNomeCategoria());

        Categoria atualiza = categoriaRepository.save(categoria);

        return new CategoriaResponseDTO(atualiza.getIdCategoria(),atualiza.getNomeCategoria(), atualiza.getAtivo());
    }
}
