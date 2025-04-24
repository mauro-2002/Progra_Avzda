package org.openapitools.dto.NotificacionDTO;

import org.openapitools.model.enums.TipoNotificacion;

public record NotificacionRequest(

        String idUsuario,
        String idReporte,
        String contenido,
        TipoNotificacion tipo

) {

}
