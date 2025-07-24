package com.br.digitalmenu.validacoes.mapper;

import com.br.digitalmenu.dto.request.ProdutoRequestDTO;
import com.br.digitalmenu.model.Produto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "Spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProdutoMapper {
    void atualizaProduto(ProdutoRequestDTO dto, @MappingTarget Produto produto);
}
