package com.br.digitalmenu.validacoes.mapper;

import com.br.digitalmenu.dto.request.ProdutoRequestDTO;
import com.br.digitalmenu.model.Produto;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-08T15:28:56-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.16 (Azul Systems, Inc.)"
)
@Component
public class ProdutoMapperImpl implements ProdutoMapper {

    @Override
    public void atualizaProduto(ProdutoRequestDTO dto, Produto produto) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getNomeProduto() != null ) {
            produto.setNomeProduto( dto.getNomeProduto() );
        }
        if ( dto.getPreco() != null ) {
            produto.setPreco( dto.getPreco() );
        }
        if ( dto.getEstoque() != null ) {
            produto.setEstoque( dto.getEstoque() );
        }
        if ( dto.getHorarioInicial() != null ) {
            produto.setHorarioInicial( dto.getHorarioInicial() );
        }
        if ( dto.getHorarioFinal() != null ) {
            produto.setHorarioFinal( dto.getHorarioFinal() );
        }
        if ( dto.getFoto() != null ) {
            produto.setFoto( dto.getFoto() );
        }
        if ( dto.getAtivo() != null ) {
            produto.setAtivo( dto.getAtivo() );
        }
    }
}
