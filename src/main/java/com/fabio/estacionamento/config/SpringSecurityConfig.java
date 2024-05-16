package com.fabio.estacionamento.config;

import com.fabio.estacionamento.jwt.JwtAuthenticationEntryPoint;
import com.fabio.estacionamento.jwt.JwtAuthorizationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebMvc
@Configuration // indica que a classe é uma classe de configuração
@EnableMethodSecurity // habilita a segurança baseada em anotações
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable) // desabilita o csrf
                .formLogin(AbstractHttpConfigurer::disable) // desabilita o form login
                .httpBasic(AbstractHttpConfigurer::disable) // desabilita o http basic
                .authorizeHttpRequests(auth -> auth // configura as autorizações
                        .requestMatchers(HttpMethod.POST, "api/v1/usuarios").permitAll() // permite o acesso a rota de cadastro de usuários para qualquer um
                        .requestMatchers(HttpMethod.POST, "api/v1/auth").permitAll() // permite o acesso a rota de autenticação para qualquer um
                        .anyRequest().authenticated() // qualquer outra requisição precisa de autenticação
                ).sessionManagement(
                        session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // configura a política de sessão para stateless
                ).addFilterBefore(
                        jwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class // adiciona o filtro de autorização
                ).exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // configura o entry point de autenticação
                ).build();
    }

    // Esse método retorna uma instância de JwtAuthorizationFilter
    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter();
    }
    // Esse método criptografa a senha do usuário no banco de dados
    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder(); // Método que criptografa a senha
    }

    // Esse método retorna uma instância de AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}