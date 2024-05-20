package com.fabio.estacionamento.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VagaResponseDto {
    private Long id;
    private String codigo;
    private String status;
}
