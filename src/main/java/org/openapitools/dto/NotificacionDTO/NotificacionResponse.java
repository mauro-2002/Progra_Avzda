package org.openapitools.dto.NotificacionDTO;

import org.openapitools.model.enums.TipoNotificacion;

public record NotificacionResponse(

        String contenido,
        String fecha,
        boolean leida,
        TipoNotificacion tipo

) {
}
