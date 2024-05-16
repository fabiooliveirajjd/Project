package com.fabio.estacionamento.web.controller;

import com.fabio.estacionamento.jwt.JwtToken;
import com.fabio.estacionamento.jwt.JwtUserDetailsService;
import com.fabio.estacionamento.web.dto.UsuarioLoginDto;
import com.fabio.estacionamento.web.dto.UsuarioResponseDto;
import com.fabio.estacionamento.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AutenticacaoController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    /**
     * Método para autenticar o usuário
     *  O usuário vai enviar uma requisição de autenticação contendo o username e o password, vamos recuperar essas informações
     *  e passar para UsernamePasswordAuthenticationToken que vai buscar os dados e buscar no banco de dados se o usuário existe,
     *  se existir retorna um objeto authenticationToken e adicona esse objeto como parte do contexto do spring security
     *  pelo método autenticatede, caso contrário, retorna uma exceção de autenticação, se passar, cai no bloco try e cria o token.
     */
    @Operation(summary = "Autenticar um usuário", description = "Recurso para autenticar um usuário")
    @PostMapping("/auth")
    public ResponseEntity<?> autenticar (@RequestBody @Valid UsuarioLoginDto dto, HttpServletRequest request) {
        log.info("processo de autenticação pelo login {}", dto.getUsername());
        try{
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(authenticationToken);
            JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());
            return ResponseEntity.ok(token);
        }catch (AuthenticationException ex) {
            log.warn("Bad Credentials from username '{}'", dto.getUsername());

        }
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais inválidas"));
    }
}

