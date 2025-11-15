package com.br.digitalmenu.service;

import com.br.digitalmenu.dto.InsertMesaDTO;
import com.br.digitalmenu.dto.response.MesaResponseDTO;
import com.br.digitalmenu.model.Mesa;
import com.br.digitalmenu.repository.MesaRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@Service
public class MesaService {

    @Autowired
    private MesaRepository mesaRepository;

    public ResponseEntity<?> insertMesa(InsertMesaDTO insertMesaDTO){
        Mesa mesa = new Mesa();

        mesa.setNumeroMesa(insertMesaDTO.numeroMesa());
        mesa.setQtdeAssentos(insertMesaDTO.qtdeAssentos());
        mesa.setAtivo(true);

        mesaRepository.save(mesa);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().build(mesa);

        return ResponseEntity.created(location).body(mesa);
    }

    public ResponseEntity<?> gerarQrCode(Long idMesa){
        if(!mesaRepository.existsById(idMesa))
            return ResponseEntity.notFound().build();

        try {
            String url = "http://localhost:4200/login?idMesa=" + idMesa;
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(url, BarcodeFormat.QR_CODE, 300, 300);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", baos);

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(baos.toByteArray());

        } catch (WriterException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> updateMesa(MesaResponseDTO mesaResponseDTO){
        Mesa mesa = mesaRepository.getReferenceById(mesaResponseDTO.idMesa());

        if(mesaResponseDTO.numeroMesa() != null && !mesaResponseDTO.numeroMesa().isBlank()){
            mesa.setNumeroMesa(mesaResponseDTO.numeroMesa());
        }

        if(mesaResponseDTO.ativo() != null){
            mesa.setAtivo(mesaResponseDTO.ativo());
        }

        if(mesaResponseDTO.qtdeAssentos() != null){
            mesa.setQtdeAssentos(mesaResponseDTO.qtdeAssentos());
        }

        mesaRepository.save(mesa);

        return ResponseEntity.noContent().build();
    }

    public List<Mesa> getAllAtivos() {
        return mesaRepository.findByAtivoTrue();
    }
}
