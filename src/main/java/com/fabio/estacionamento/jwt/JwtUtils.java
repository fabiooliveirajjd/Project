package com.fabio.estacionamento.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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
    private static javax.crypto.SecretKey generateKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)); // gera a chave secreta
    }


    //método que faz o cáuculo entre a data que o token foi criado e a data que deve ser expirado
    private static Date toExpireDate (Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime(); // converte a data para LocalDateTime definidas na config
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES); // adiciona os dias, horas e minutos
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant()); // retorna a data final
    }

    //método que vai gerar o token
    public static JwtToken createToken(String username, String role) {
        Date issuedAt = new Date(); // data de criação do token
        Date limit = toExpireDate(issuedAt); // data de expiração do token
        String token = Jwts.builder() // cria o token
                .header().add("typ", "JWT")// adiciona o tipo do token
                .and()
                .subject(username) // adiciona o usuário
                .issuedAt(issuedAt) // adiciona a data de criação
                .expiration(limit) // adiciona a data de expiração
                .signWith(generateKey()) // adiciona a chave secreta
                .claim("role", role) // adiciona a regra
                .compact();
        return new JwtToken(token);
    }

    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(generateKey())
                    .build()
                    .parseSignedClaims(refectorToken(token)).getPayload();
        } catch (JwtException ex) {
            log.error(String.format("Token invalido %s", ex.getMessage()));
        }
        return null;
    }


    //método que vai retornar o usuário do token
    public static boolean isTokenValid(String token) {
        try {
            Jwts.parser() // faz a verificação do token
                    .verifyWith(generateKey()) // verifica com a chave secreta
                    .build()
                    .parseSignedClaims(refectorToken(token)); // faz o parse do token
            return true;
        } catch (JwtException ex) {
            log.error(String.format("Token invalido %s", ex.getMessage())); // loga o erro
        }
        return false;
    }

    //método que remove o Bearer do token
    private static String refectorToken(String token) {
        if (token.contains(JWT_BEARER)) { // verifica se o token contém o Bearer
            return token.substring(JWT_BEARER.length()); // remove o Bearer
        }
        return token;
    }


}
