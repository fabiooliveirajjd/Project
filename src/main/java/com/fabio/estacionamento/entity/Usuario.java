package com.fabio.estacionamento.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@EntityListeners(AuditingEntityListener.class)
public class Usuario implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @Column(name = "username", nullable = false, unique = true, length = 100) // username é unico e não pode ser nulo
    private String username;
    @Column(name = "password", nullable = false, length = 200) // password não pode ser nulo e tem tamanho 200 (criptografado)
    private String password;
    @Enumerated(EnumType.STRING) // formato de enumeração string
    @Column(name = "role", nullable = false, length = 25) // role não pode ser nulo e tem tamanho 25
    private Role role = Role.ROLE_CLIENTE; // role padrão é cliente

    @CreatedDate // Anotação para data de criação
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao; // hora do momento que o usuario foi criado
    @LastModifiedDate //
    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao; // hora do momento que o usuario foi modificado
    @CreatedBy
    @Column(name = "criado_por")
    private String criadoPor; // quem criou o usuario
    @LastModifiedBy
    @Column(name = "modificado_por")
    private String modificadoPor; // quem modificou o usuario

    //enun de roles
    public enum Role {
        ROLE_ADMIN, ROLE_CLIENTE
    }

    //construtor equals do id
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    //construtor hashCode do id
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    //construtor toString do id
    @Override
    public String toString() {
        return "Usuario{" + "id=" + id + '}';
    }
}
