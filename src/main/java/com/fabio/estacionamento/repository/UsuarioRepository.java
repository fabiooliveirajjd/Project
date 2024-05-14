package com.fabio.estacionamento.repository;

import com.fabio.estacionamento.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

    // não pode ser feita com palavra chave findBy, pois não é um atributo do objeto Usuario
    @Query("SELECT u.role FROM Usuario u WHERE u.username like :username")
    Usuario.Role findRoleByUsername(String username);


}
