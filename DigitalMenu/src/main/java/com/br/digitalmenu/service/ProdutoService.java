package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.request.ProdutoRequestDTO;
import com.br.digitalmenu.dto.response.ProdutoResponseDTO;
import com.br.digitalmenu.model.DiaSemana;
import com.br.digitalmenu.model.Ingrediente;
import com.br.digitalmenu.model.Produto;
import com.br.digitalmenu.model.Restricao;
import com.br.digitalmenu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private IngredienteRepository ingredienteRepository;

    @Autowired
    private DiaSemanaRepository diaSemanaRepository;

    @Autowired
    private RestricaoRepository restricaoRepository;

    @Autowired
    private CloudinaryService cloudinaryService;


    public List<ProdutoResponseDTO> findAll() {
        return produtoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }

    public ProdutoResponseDTO findById(Long id) {
        return produtoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
    }

    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrato"));

        if (dto.getPreco() != null) {
            produto.setPreco(dto.getPreco());
        }

        if (dto.getIdCategoria() != null) {
            produto.setCategoria(categoriaRepository.findById(dto.getIdCategoria())
                    .orElseThrow(() -> new RuntimeException("Categoria nao encontraa")));
        }

        if (dto.getIngredientesIds() != null) {
            produto.setIngrediente(ingredienteRepository.findAllById(dto.getIngredientesIds()));
        }

        if (dto.getRestricoesIds() != null) {
            produto.setRestricao(restricaoRepository.findAllById(dto.getRestricoesIds()));
        }

        if (dto.getDiasSemanaIds() != null) {
            produto.setDiasSemana(diaSemanaRepository.findAllById(dto.getDiasSemanaIds()));
        }
        Produto atualizado = produtoRepository.save(produto);
        return toDTO(atualizado);
    }

    public void desativarProduto(Long id) {
        produtoRepository.findById(id).map(produto -> {
            produto.setAtivo(false);
            return produtoRepository.save(produto);
        }).orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
    }


    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto){
        Produto produto = new Produto();
        produto.setNomeProduto(dto.getNomeProduto());
        produto.setPreco(dto.getPreco());
        produto.setHorarioInicial(dto.getHorarioInicial());
        produto.setHorarioFinal(dto.getHorarioFinal());
        produto.setFoto(dto.getFoto());
        produto.setEstoque(dto.getEstoque());
        produto.setAtivo(dto.getAtivo());

        produto.setCategoria(categoriaRepository.findById(dto.getIdCategoria())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada")));

        if (!dto.getIngredientesIds().isEmpty()) {
            produto.setIngrediente(ingredienteRepository.findAllById(dto.getIngredientesIds()));
        }

        if (!dto.getRestricoesIds().isEmpty()) {
            produto.setRestricao(restricaoRepository.findAllById(dto.getRestricoesIds()));
        }

        produto.setDiasSemana(diaSemanaRepository.findAllById(dto.getDiasSemanaIds()));

        Produto salvo = produtoRepository.save(produto);
        return toDTO(salvo);
    }


    private ProdutoResponseDTO toDTO(Produto produto) {
        return ProdutoResponseDTO.builder()
                .idProduto(produto.getIdProduto())
                .nomeProduto(produto.getNomeProduto())
                .preco(produto.getPreco())
                .horarioInicial(produto.getHorarioInicial())
                .horarioFinal(produto.getHorarioFinal())
                .foto(produto.getFoto())
                .estoque(produto.getEstoque())
                .ativo(produto.getAtivo())
                .nomeCategoria(produto.getCategoria().getNomeCategoria())
                .ingredientes(Optional.ofNullable(produto.getIngrediente())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(Ingrediente::getNomeIngrediente)
                        .collect(Collectors.toList()))
                .restricoes(
                        Stream.concat(
                                        // restrições diretas do produto
                                        Optional.ofNullable(produto.getRestricao())
                                                .orElse(Collections.emptyList())
                                                .stream()
                                                .map(Restricao::getNomeRestricao),
                                        // restrições vindas dos ingredientes
                                        Optional.ofNullable(produto.getIngrediente())
                                                .orElse(Collections.emptyList())
                                                .stream()
                                                .flatMap(i -> Optional.ofNullable(i.getRestricoes())
                                                        .orElse(Collections.emptyList())
                                                        .stream()
                                                        .map(Restricao::getNomeRestricao))
                                )
                                .distinct() // remove duplicados
                                .collect(Collectors.toList())
                )
                .diasSemana(produto.getDiasSemana().stream()
                        .map(DiaSemana::getNomeDia)
                        .collect(Collectors.toList()))
                .build();
    }
}
