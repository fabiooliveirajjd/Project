package com.fabio.estacionamento.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration // indica que a classe é uma classe de configuração
@EnableMethodSecurity // habilita a segurança baseada em anotações
public class SpringSecurityConfig {


}