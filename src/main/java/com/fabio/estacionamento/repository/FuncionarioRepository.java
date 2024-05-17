package com.fabio.estacionamento.repository;

import com.fabio.estacionamento.entity.Funcionario;
import com.fabio.estacionamento.repository.projection.FuncionarioProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @Query("select f from Funcionario f")
    Page<FuncionarioProjection> findAllPageable(Pageable pageable);

    Funcionario findByUsuarioId(Long id);
}
