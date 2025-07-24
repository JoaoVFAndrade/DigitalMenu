package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.InsertRestricaoDTO;
import com.br.digitalmenu.dto.RestricaoDTO;
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

    public ResponseEntity<?> insertRestricao(InsertRestricaoDTO insertRestricaoDTO) {

        Restricao restricao = new Restricao();

        restricao.setNomeRestricao(insertRestricaoDTO.nomeRestricao());

        restricaoRepository.save(restricao);

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().build(restricao);

        return ResponseEntity.created(location).body(restricao);
    }

    public ResponseEntity<?> deleteRestricao(Long idRestricao) {
        if (idRestricao == 1)
            return ResponseEntity.badRequest().body("Esta restrição não pode ser deletada");

        Restricao restricao = restricaoRepository.getReferenceById(idRestricao);

        restricao.setAtivo(false);

        restricaoRepository.save(restricao);

        return ResponseEntity.ok("Restricao deleteda com sucesso");
    }

    public ResponseEntity<?> editarRestricao(RestricaoDTO restricaoDTO){
        if(restricaoDTO.idRestricao() == 1){
            return ResponseEntity.badRequest().body("Esta restrição não pode ser alterada");
        }

        Restricao restricao = restricaoRepository.getReferenceById(restricaoDTO.idRestricao());

        if(restricaoDTO.nomeRestricao() != null && !restricaoDTO.nomeRestricao().isEmpty())
            restricao.setNomeRestricao(restricaoDTO.nomeRestricao());

        restricaoRepository.save(restricao);

        return ResponseEntity.ok("Restrição alterada com sucesso");
    }
}
