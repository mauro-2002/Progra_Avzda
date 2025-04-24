package org.openapitools.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.openapitools.dto.NotificacionDTO.NotificacionRequest;
import org.openapitools.dto.NotificacionDTO.NotificacionResponse;
import org.openapitools.model.Notificacion;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NotificacionMapper {

    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "leida", constant = "false")
    @Mapping(target = "fecha", expression = "java(java.time.LocalDateTime.now())")
    Notificacion parseToNotificacion(NotificacionRequest notificacion);

    NotificacionResponse toNotificacionResponse(Notificacion notificacion);
}
