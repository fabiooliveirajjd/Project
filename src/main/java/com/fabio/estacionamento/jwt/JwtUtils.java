package com.fabio.estacionamento.jwt;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class JwtUtils {

    public static final String JWT_BEARER = "Bearer "; // Bearer é o tipo de autenticação
    public static final String JWT_AUTHORIZATION = "Authorization"; // Authorization é o cabeçalho da requisição
    public static final String SECRET_KEY = "0123456789-0123456789-0123456789"; // chave secreta para criptografia
    public static final long EXPIRE_DAYS = 0; // tempo de expiração do token em dias
    public static final long EXPIRE_HOURS = 0; // tempo de expiração do token em horas
    public static final long EXPIRE_MINUTES = 2; // tempo de expiração do token em minutos

    private JwtUtils(){
    }
}
