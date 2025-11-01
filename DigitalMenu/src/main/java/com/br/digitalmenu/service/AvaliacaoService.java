package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.RelatorioAvaliacaoDTO;
import com.br.digitalmenu.dto.request.InsertAvaliacaoDTO;
import com.br.digitalmenu.dto.response.AvaliacaoReponseDTO;
import com.br.digitalmenu.model.Avaliacao;
import com.br.digitalmenu.repository.AvaliacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    public ResponseEntity<?> insertAvaliacao(InsertAvaliacaoDTO dto){
        Avaliacao avaliacao = new Avaliacao();

        avaliacao.setData(LocalDate.now());
        avaliacao.setHorario(LocalTime.now());

        avaliacao.setComentario(dto.comentario());
        avaliacao.setNotaAmbiente(dto.notaAmbiente());
        avaliacao.setNotaAtendimento(dto.notaAtendimento());
        avaliacao.setNotaQualidadeDosProduto(dto.notaQualidadeDosProdutos());

        avaliacaoRepository.save(avaliacao);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build(avaliacao);

        AvaliacaoReponseDTO avaliacaoReponseDTO = new AvaliacaoReponseDTO(avaliacao);

        return ResponseEntity.created(location).body(avaliacaoReponseDTO);
    }

    public ResponseEntity<?> getAvaliacoes(LocalDate dataInicial, LocalDate dataFinal){
        List<AvaliacaoReponseDTO> avaliacoes = avaliacaoRepository.findAll().stream().
                filter(avaliacao -> dataInicial == null ||( avaliacao.getData().isAfter(dataInicial) || avaliacao.getData().isEqual(dataInicial))).
                filter(avaliacao -> dataFinal == null ||( avaliacao.getData().isBefore(dataFinal) || avaliacao.getData().isEqual(dataFinal))).map(AvaliacaoReponseDTO::new).toList();

        if(avaliacoes.isEmpty()) return ResponseEntity.ok("Nenhuma avaliação encontrada");

        double mediaNotaAtendimento = (double) avaliacoes.stream()
                .mapToInt(AvaliacaoReponseDTO::notaAtendimento)
                .sum() /avaliacoes.size();

        double mediaNotaQualidadeProduto = (double) avaliacoes.stream()
                .mapToInt(AvaliacaoReponseDTO::notaQualidadeDosProdutos)
                .sum() /avaliacoes.size();

        double mediaNotaAmbiente = (double) avaliacoes.stream()
                .mapToInt(AvaliacaoReponseDTO::notaAtendimento)
                .sum() /avaliacoes.size();

        return ResponseEntity.ok(new RelatorioAvaliacaoDTO(avaliacoes.size(),mediaNotaAtendimento,mediaNotaQualidadeProduto,mediaNotaAmbiente,avaliacoes));


    }
}
