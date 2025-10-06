package com.br.digitalmenu.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ResetTokenInfo {
    private String email;
    private LocalDateTime expira;
}
