package com.fabio.estacionamento.web.controller;

import com.fabio.estacionamento.entity.Funcionario;
import com.fabio.estacionamento.jwt.JwtUserDatails;
import com.fabio.estacionamento.repository.projection.FuncionarioProjection;
import com.fabio.estacionamento.service.FuncionarioService;
import com.fabio.estacionamento.service.UsuarioService;
import com.fabio.estacionamento.web.dto.FuncionarioCreateDto;
import com.fabio.estacionamento.web.dto.FuncionarioResponseDto;
import com.fabio.estacionamento.web.dto.PageableDto;
import com.fabio.estacionamento.web.dto.mapper.FuncionarioMapper;
import com.fabio.estacionamento.web.dto.mapper.PageableMapper;
import com.fabio.estacionamento.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

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
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = FuncionarioResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Funcionario CPF já possui cadastro no sistema",
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


    @Operation(summary = "Localizar um funcionario", description = "Recurso para localizar um funcionario pelo ID. " +
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = FuncionarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Funcionario não encontrado",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<FuncionarioResponseDto> findById(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(FuncionarioMapper.toDto(funcionario));
    }

    @Operation(summary = "Recuperar lista de funcionarios",
            description = "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN' ",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Representa o total de elementos por página"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true, // hidden = true para não aparecer na documentação do swagger
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "nome,asc")),
                            description = "Representa a ordenação dos resultados. Aceita multiplos critérios de ordenação são suportados.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = FuncionarioResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> getAll(@Parameter(hidden = true)
                                              // hidden = true para não aparecer na documentação do swagger
                                              @PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) {
        Page<FuncionarioProjection> funcionario = funcionarioService.buscarTodos(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(funcionario));
    }


    @Operation(summary = "Recuperar dados do funcionario autenticado",
            description = "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = FuncionarioResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de ADMIN",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping("/detalhes")
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<FuncionarioResponseDto> getDetalhes(@AuthenticationPrincipal JwtUserDatails userDetails) {
        Funcionario funcionario = funcionarioService.buscarPorUsuarioId(userDetails.getId());
        return ResponseEntity.ok(FuncionarioMapper.toDto(funcionario));
    }
}
