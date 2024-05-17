package com.fabio.estacionamento.repository;

import com.fabio.estacionamento.entity.Funcionario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @Query("select f from Funcionario f")
    Page<Funcionario> findAllPageable(Pageable pageable);

    Funcionario findByUsuarioId(Long id);
}
