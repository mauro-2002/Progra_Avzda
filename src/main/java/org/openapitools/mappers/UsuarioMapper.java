package org.openapitools.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.openapitools.dto.UsuarioDTO.UsuarioRequest;
import org.openapitools.dto.UsuarioDTO.UsuarioResponse;
import org.openapitools.model.UsuarioStandar;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "status", constant = "REGISTRADO")
    @Mapping(target = "reportes", ignore = true)
    @Mapping(target = "notificaciones", ignore = true)
    @Mapping(target = "preferencias", ignore = true)
    @Mapping(target = "password" , expression = "java( new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(userDTO.password()) )")
    @Mapping(target = "codigoActivacion", ignore = true)
    @Mapping(target = "expiracionCodigo", ignore = true)
    @Mapping(target = "codigoRecuperacion", ignore = true)
    UsuarioStandar parseOf(UsuarioRequest userDTO);


    UsuarioResponse toUserResponse(UsuarioStandar user);
}
