package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.InsertRestricaoDTO;
import com.br.digitalmenu.model.Restricao;
import com.br.digitalmenu.repository.RestricaoRepository;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Service
public class RestricaoService {

    @Autowired
    private RestricaoRepository restricaoRepository;

    public ResponseEntity<?> insertRestricao(InsertRestricaoDTO insertRestricaoDTO){

        Restricao restricao = new Restricao();

        restricao.setNomeRestricao(insertRestricaoDTO.nomeRestricao());

        restricaoRepository.save(restricao);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().build(restricao);

        return ResponseEntity.created(location).body(restricao);
    }
}
