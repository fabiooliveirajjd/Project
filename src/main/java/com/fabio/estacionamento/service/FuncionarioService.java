package com.fabio.estacionamento.service;

import com.fabio.estacionamento.entity.Funcionario;
import com.fabio.estacionamento.exception.CpfUniqueViolationException;
import lombok.RequiredArgsConstructor;
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
}
