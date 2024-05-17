package com.fabio.estacionamento.web.dto.mapper;

import com.fabio.estacionamento.entity.Cliente;
import com.fabio.estacionamento.web.dto.ClienteCreateDto;
import com.fabio.estacionamento.web.dto.ClienteResponseDto;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE) // AccessLevel.PRIVATE é para garantir que a classe não seja instanciada
public class ClienteMapper {

    public static Cliente toCliente(ClienteCreateDto dto) {
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toDto(Cliente cliente) {
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }
}
