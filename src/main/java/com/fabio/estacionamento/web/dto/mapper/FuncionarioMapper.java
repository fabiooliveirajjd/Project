package com.fabio.estacionamento.web.dto.mapper;


import com.fabio.estacionamento.entity.Funcionario;
import com.fabio.estacionamento.web.dto.FuncionarioResponseDto;
import com.fabio.estacionamento.web.dto.FuncionarioCreateDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // AccessLevel.PRIVATE é para garantir que a classe não seja instanciada
public class FuncionarioMapper {

    public static Funcionario toFuncionario(FuncionarioCreateDto dto) {
        return new ModelMapper().map(dto, Funcionario.class);
    }

    public static FuncionarioResponseDto toDto(Funcionario funcionario) {
        return new ModelMapper().map(funcionario, FuncionarioResponseDto.class);
    }

}
