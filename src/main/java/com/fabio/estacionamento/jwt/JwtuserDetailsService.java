package com.fabio.estacionamento.jwt;

import com.fabio.estacionamento.entity.Usuario;
import com.fabio.estacionamento.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
// UserDetailsService é uma classe do Spring Security que é usada para localizar um usuários no banco de dados
public class JwtuserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioService.buscarPorUsername(username);
    }
}
