package org.openapitools.services.interfaces;

import org.openapitools.dto.NotificacionDTO.NotificacionRequest;
import org.openapitools.dto.NotificacionDTO.NotificacionResponse;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.model.Notificacion;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface NotificacionService {

    Optional<SuccessResponse> sendNotificacion(Long idUsuario, NotificacionRequest notificacionRequest);

    Optional<NotificacionResponse> getNotificacion(NotificacionRequest notificacionRequest);

    void markAsView(Long idNoti, Long idUsuario);
}
