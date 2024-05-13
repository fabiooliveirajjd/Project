package com.fabio.estacionamento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration // indica que a classe é uma classe de configuração
@EnableMethodSecurity // habilita a segurança baseada em anotações
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable()) // desabilita o csrf
                .formLogin(form -> form.disable()) // desabilita o formulario de login
                .httpBasic(basic -> basic.disable()) // desabilita o http basic authentication
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "api/v1/usuarios").permitAll() // permite o acesso ao endpoint de cadastro de usuários
                        .anyRequest().authenticated() // qualquer outra requisição deve ser autenticada
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // desabilita o uso de sessão
                ).build();
    }

}