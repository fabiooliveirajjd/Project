package com.fabio.estacionamento.web.dto.mapper;

import com.fabio.estacionamento.entity.Usuario;
import com.fabio.estacionamento.web.dto.UsuarioCreateDto;
import com.fabio.estacionamento.web.dto.UsuarioResponseDto;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UsuarioMapper {

    // Método que mapeia as propriedades UsuarioCreateDto para Usuario
    public static Usuario toUsuario(UsuarioCreateDto createDto) {
        return new ModelMapper().map(createDto, Usuario.class);
    }

    // Método que mapeia as propriedades Usuario para UsuarioResponseDto
    public static UsuarioResponseDto toDto(Usuario usuario) {
        String role = usuario.getRole().name().substring("ROLE_".length()); // Obtém somente o nome do papel do usuário, removendo o prefixo ROLE_
        PropertyMap<Usuario, UsuarioResponseDto> props = new PropertyMap<Usuario, UsuarioResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        }; // Mapeia a propriedade role do objeto Usuario para a propriedade role do objeto UsuarioResponseDto
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);
        return mapper.map(usuario, UsuarioResponseDto.class);
    }

    // Método que mapeia uma lista de objetos Usuario para uma lista de objetos UsuarioResponseDto
    public static List<UsuarioResponseDto> toListDto(List<Usuario> usuarios) {
        return usuarios.stream().map(user -> toDto(user)).collect(Collectors.toList()); // Mapeia cada objeto Usuario para um objeto UsuarioResponseDto e retorna uma lista de objetos UsuarioResponseDto
    }
}
