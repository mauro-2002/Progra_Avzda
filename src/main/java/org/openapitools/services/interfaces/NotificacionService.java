package org.openapitools.services.interfaces;

import org.openapitools.dto.NotificacionDTO.NotificacionRequest;
import org.openapitools.dto.NotificacionDTO.NotificacionResponse;
import org.openapitools.dto.SuccessResponse;

import java.util.List;
import java.util.Optional;

public interface NotificacionService {

    Optional<SuccessResponse> sendNotificacion(String idUsuario, NotificacionRequest notificacionRequest);

    Optional<List<NotificacionResponse>> getNotificaciones(NotificacionRequest notificacionRequest);

    void markAsView(String idNoti, String idUsuario);
}
