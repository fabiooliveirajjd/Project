package com.fabio.estacionamento.jwt;

import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

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
    private JwtUtils() {
    }

    // método que prepara a chave secreta para a criptografia
    private static Key getSecretKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)); // retorna a chave secreta
    }

    //método que faz o cáuculo entre a data que o token foi criado e a data que deve ser expirado
    private static Date toExpireDate (Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); // converte a data para LocalDateTime definidas na config
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES); // adiciona os dias, horas e minutos
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant()); // retorna a data final
    }
}
