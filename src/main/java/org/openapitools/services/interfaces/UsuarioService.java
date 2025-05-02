package org.openapitools.services.interfaces;

import org.openapitools.dto.*;
import org.openapitools.dto.UsuarioDTO.*;
import org.openapitools.model.Notificacion;
import org.openapitools.model.Usuario;
import org.openapitools.model.UsuarioStandar;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    UsuarioResponse createUsuario(UsuarioRequest usuarioRequest);

    UsuarioResponse updateUsuario(String id, UsuarioUpdateRequest usuarioUpdateRequest);

    SuccessResponse deleteUsuario(String id);

    UsuarioResponse getUsuario(String id);

    List<Notificacion> getNotificacionesUsuario(String id);

    Optional<UsuarioResponse> LogIn(LoginRequest loginRequest);

    Optional<SuccessResponse> activarUsuario(UsuarioStandar usuario, ActivarCuentaRequest activarCuentaRequest);

    void recuperarPassword (RecuperarPasswordRequest request);

    Optional<SuccessResponse> resetPassword (ResetPasswordRequest resetPasswordRequest);
}
