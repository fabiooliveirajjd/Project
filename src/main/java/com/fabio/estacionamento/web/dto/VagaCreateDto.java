package com.fabio.estacionamento.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VagaCreateDto {
    @NotBlank
    @Size(min = 4, max = 4)
    private String codigo;
    @NotBlank
    @Pattern(regexp = "LIVRE|OCUPADA") // Apenas aceita LIVRE ou OCUPADA caso contrário lança uma exceção
    private String status;
}
