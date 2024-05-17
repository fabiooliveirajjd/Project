package com.fabio.estacionamento.web.controller;

import com.fabio.estacionamento.entity.Funcionario;
import com.fabio.estacionamento.jwt.JwtUserDatails;
import com.fabio.estacionamento.service.FuncionarioService;
import com.fabio.estacionamento.service.UsuarioService;
import com.fabio.estacionamento.web.dto.ClienteResponseDto;
import com.fabio.estacionamento.web.dto.FuncionarioCreateDto;
import com.fabio.estacionamento.web.dto.FuncionarioResponseDto;
import com.fabio.estacionamento.web.dto.mapper.FuncionarioMapper;
import com.fabio.estacionamento.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Funcionários", description = "Contém todas as operações relativas ao recurso de um funcionário")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/funcionarios")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo funcionario",
            description = "Recurso para criar um novo funcionario vinculado a um usuário cadastrado. " +
                    "Requisição exige uso de um bearer token. Acesso restrito a Role='FUNCIONARIO'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Cliente CPF já possui cadastro no sistema",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<FuncionarioResponseDto> create(@RequestBody @Valid FuncionarioCreateDto dto,
                                                         @AuthenticationPrincipal JwtUserDatails userDetails) {
        Funcionario funcionario = FuncionarioMapper.toFuncionario(dto);
        funcionario.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        funcionarioService.salvar(funcionario);
        return ResponseEntity.status(201).body(FuncionarioMapper.toDto(funcionario));
    }
}
