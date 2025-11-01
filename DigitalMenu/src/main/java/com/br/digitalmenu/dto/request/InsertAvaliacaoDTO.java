package com.br.digitalmenu.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record InsertAvaliacaoDTO(
        @NotNull
        @Max(5)
        @Min(1)
        @Schema(example = "4", type = "integer", minimum = "1", maximum = "5")
        Byte notaAtendimento,
        @NotNull
        @Max(5)
        @Min(1)
        @Schema(example = "4", type = "integer", minimum = "1", maximum = "5")
        Byte notaAmbiente,
        @NotNull
        @Max(5)
        @Min(1)
        @Schema(example = "4", type = "integer", minimum = "1", maximum = "5")
        Byte notaQualidadeDosProdutos,
        @Size(max = 50)
        String comentario

) {
}
