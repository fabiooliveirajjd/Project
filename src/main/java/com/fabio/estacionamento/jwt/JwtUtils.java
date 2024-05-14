package com.fabio.estacionamento.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;

@Slf4j
public class JwtUtils {

    //Constantes que serão usadas no projeto
    public static final String JWT_BEARER = "Bearer "; // Bearer é o tipo de autenticação
    public static final String JWT_AUTHORIZATION = "Authorization"; // Authorization é o cabeçalho da requisição

    // chave secreta com no mínimo 32 caracteres para garantir o processo de criptografia (Menos que 32 gera exceção)
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789";
    public static final long EXPIRE_DAYS = 0; // tempo de expiração do token em dias
    public static final long EXPIRE_HOURS = 0; // tempo de expiração do token em horas
    public static final long EXPIRE_MINUTES = 2; // tempo de expiração do token em minutos

    //construtor privado para garantir que a classe não seja instanciada
    private JwtUtils(){
    }

    // método que retorna a chave secreta
    private static Key getSecretKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
