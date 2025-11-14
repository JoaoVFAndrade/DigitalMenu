package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.request.ProdutoRequestDTO;
import com.br.digitalmenu.dto.response.IngredienteResponseDTO;
import com.br.digitalmenu.dto.response.ProdutoResponseDTO;
import com.br.digitalmenu.model.*;
import com.br.digitalmenu.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.Collection;
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

    @Autowired
    private ClienteRepository clienteRepository;


    public List<ProdutoResponseDTO> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        boolean isCliente = roles.stream()
                .anyMatch(r -> r.getAuthority().equals("CLIENTE"));


        boolean isAdmin = roles.stream()
                .anyMatch(r -> r.getAuthority().equals("FUNCIONARIO_ADM"));

        if(isAdmin) return produtoRepository.findAll().stream().map(this::toDTO).collect(Collectors.toList());

        Cliente cliente = clienteRepository.findByEmail(authentication.getName()).get();

        List<Produto> produtos = produtoRepository.findAll().stream()
                .filter(produto -> produto.getDiasSemana().stream()
                        .map(DiaSemana::getIdDia)
                        .anyMatch(idDia -> idDia.equals(Long.parseLong(String.valueOf(LocalDate.now().getDayOfWeek().getValue())))))
                .filter(produto -> {
                    return estaEntre(produto.getHorarioInicial(),produto.getHorarioFinal());
                })
                .toList();

        if(!isCliente) return produtos.stream().map(this::toDTO).toList();

        return produtos.stream()
                .filter(produto ->
                        produto.getRestricao().stream()
                                .noneMatch(restricao -> cliente.getRestricoes().contains(restricao))
                                &&
                                produto.getIngrediente().stream()
                                        .flatMap(ingrediente -> ingrediente.getRestricoes().stream())
                                        .noneMatch(restricao -> cliente.getRestricoes().contains(restricao))
                )
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public ProdutoResponseDTO findById(Long id) {
        return produtoRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrado"));
    }

    public ProdutoResponseDTO atualizarProduto(Long id, ProdutoRequestDTO dto) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto nao encontrato"));

        if (dto.getNomeProduto() != null && !dto.getNomeProduto().isBlank()) {
            produto.setNomeProduto(dto.getNomeProduto());
        }
        if (dto.getPreco() != null) {
            produto.setPreco(dto.getPreco());
        }
        if (dto.getFoto() != null) {
            produto.setFoto(dto.getFoto());
        }
        if (dto.getEstoque() != null) {
            produto.setEstoque(dto.getEstoque());
        }
        if (dto.getAtivo() != null) {
            produto.setAtivo(dto.getAtivo());
        }

        if (dto.getHorarioInicial() != null) {
            produto.setHorarioInicial(dto.getHorarioInicial());
        }
        if (dto.getHorarioFinal() != null) {
            produto.setHorarioFinal(dto.getHorarioFinal());
        }

        if(dto.getDescricao() != null){
            produto.setDescricao(produto.getDescricao());
        }

        if (dto.getIdCategoria() != null) {
            produto.setCategoria(
                    categoriaRepository.findById(dto.getIdCategoria())
                            .orElseThrow(() -> new RuntimeException("Categoria nao encontraa"))
            );
        }

        if (dto.getIngredientesIds() != null) {
            produto.setIngrediente(List.of());
            produto.setIngrediente(ingredienteRepository.findAllById(dto.getIngredientesIds()));
        }
        if (dto.getRestricoesIds() != null) {
            produto.setRestricao(List.of());
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
        produto.setDescricao(dto.getDescricao());

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
        List<IngredienteResponseDTO> ingredientesDTO = Optional.ofNullable(produto.getIngrediente())
                .orElse(Collections.emptyList())
                .stream()
                .map(IngredienteResponseDTO::new).toList();

        List<String> restricoesProduto = Optional.ofNullable(produto.getRestricao())
                .orElse(Collections.emptyList())
                .stream()
                .map(Restricao::getNomeRestricao)
                .collect(Collectors.toList());

        List<String> restricoesCombinadas = Stream.concat(
                        restricoesProduto.stream(),
                        ingredientesDTO.stream()
                                .flatMap(i -> i.getRestricoes().stream())
                )
                .distinct()
                .collect(Collectors.toList());

        return ProdutoResponseDTO.builder()
                .idProduto(produto.getIdProduto())
                .nomeProduto(produto.getNomeProduto())
                .descricao(produto.getDescricao())
                .preco(produto.getPreco())
                .horarioInicial(produto.getHorarioInicial())
                .horarioFinal(produto.getHorarioFinal())
                .foto(produto.getFoto())
                .estoque(produto.getEstoque())
                .ativo(produto.getAtivo())
                .nomeCategoria(produto.getCategoria().getNomeCategoria())
                .ingredientes(ingredientesDTO) // agora com restrições dentro
                .restricoesProduto(restricoesProduto)
                .restricoesCombinadas(restricoesCombinadas)
                .diasSemana(Optional.ofNullable(produto.getDiasSemana())
                        .orElse(Collections.emptyList())
                        .stream()
                        .map(DiaSemana::getNomeDia)
                        .collect(Collectors.toList()))
                .build();
    }

    private boolean estaEntre(LocalTime inicio, LocalTime fim) {
        if(inicio.equals(LocalTime.of(0,0,0)) && fim.equals(LocalTime.of(0,0,0)))
            return true;

        LocalTime horario = LocalTime.now();
        if (inicio.isAfter(fim)) {
            LocalTime temp = inicio;
            inicio = fim;
            fim = temp;
        }

        return !horario.isBefore(inicio) && !horario.isAfter(fim);
    }
}
