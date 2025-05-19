package org.openapitools.services.interfaces;

import org.openapitools.dto.UsuarioDTO.*;
import org.openapitools.model.Notificacion;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse createUsuario(UsuarioRequest usuarioRequest);

    UsuarioResponse updateUsuario(String id, UsuarioUpdateRequest usuarioUpdateRequest);

    UsuarioResponse deleteUsuario(String id);

    UsuarioResponse getUsuario(String id);

    List<Notificacion> getNotificacionesUsuario(String id);

}
