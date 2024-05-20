package com.fabio.estacionamento.web.controller;

import com.fabio.estacionamento.entity.Vaga;
import com.fabio.estacionamento.service.VagaService;
import com.fabio.estacionamento.web.dto.VagaCreateDto;
import com.fabio.estacionamento.web.dto.VagaResponseDto;
import com.fabio.estacionamento.web.dto.mapper.VagaMapper;
import com.fabio.estacionamento.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpHeaders;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Vagas", description = "Contém todas as opereções relativas ao recurso de uma vaga")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;

    @Operation(summary = "Criar uma nova vaga", description = "Recurso para criar uma nova vaga." +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado")),
                    @ApiResponse(responseCode = "409", description = "Vaga já cadastrada",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    // ResponseEntity tipo Void porque não retorna corpo na resposta e sim o cabeçalho do recurso criado
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDto dto) {
        Vaga vaga = VagaMapper.toVaga(dto); // converte dto para entidade
        vagaService.salvar(vaga); // salva a entidade
        URI location = ServletUriComponentsBuilder // cria a URI do recurso criado
                .fromCurrentRequestUri().path("/{codigo}")// pega a URI atual e concatena com o código da vaga
                .buildAndExpand(vaga.getCodigo()) // substitui o {codigo} pelo código da vaga
                .toUri(); // converte para URI
        return ResponseEntity.created(location).build(); // retorna a URI do recurso criado
    }

    @Operation(summary = "Localizar uma vaga", description = "Recurso para retornar uma vaga pelo seu código" +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = VagaResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Vaga não localizada",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto> getByCodigo(@PathVariable String codigo) {
        Vaga vaga = vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));
    }

}
