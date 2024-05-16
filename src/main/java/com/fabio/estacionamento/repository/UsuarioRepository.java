package com.fabio.estacionamento.repository;

import com.fabio.estacionamento.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    // SELECT u.role FROM estacionamento.usuarios u
    // WHERE u.username LIKE 'fabio@mail.com';
    @Query("SELECT u.role FROM Usuario u WHERE u.username like :username")
    Usuario.Role findRoleByUsername(String username);


}
