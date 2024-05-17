package com.fabio.estacionamento.service;

import com.fabio.estacionamento.entity.Funcionario;
import com.fabio.estacionamento.exception.CpfUniqueViolationException;
import com.fabio.estacionamento.exception.EntityNotFoundException;
import com.fabio.estacionamento.repository.projection.FuncionarioProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.fabio.estacionamento.repository.FuncionarioRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

@RequiredArgsConstructor
@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        try{
            return funcionarioRepository.save(funcionario);
        }catch (DataIntegrityViolationException ex) {
            throw new CpfUniqueViolationException(
                    String.format("CPF '%s' não pode ser cadastrado, já existe no sistema", funcionario.getCpf())
            );
        }
    }

    @Transactional(readOnly = true)
    public Funcionario buscarPorId(Long id) {
        return funcionarioRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Funcionario id=%s não encontrado no sistema", id))
        );
    }

    @Transactional(readOnly = true)
    public Page<FuncionarioProjection> buscarTodos(Pageable pageable) {
        return funcionarioRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Funcionario buscarPorUsuarioId(Long id) {
        return funcionarioRepository.findByUsuarioId(id);
    }
}
