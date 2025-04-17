package org.openapitools.services.interfaces;

import org.openapitools.dto.*;
import org.openapitools.model.Notificacion;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    UsuarioResponse createUsuario(UsuarioRequest usuarioRequest);

    UsuarioResponse updateUsuario(Long id, UsuarioUpdateRequest usuarioUpdateRequest);

    SuccessResponse deleteUsuario(Long id);

    UsuarioResponse getUsuario(Long id);

    List<Notificacion> getNotificacionesUsuario(Long id);

    Optional<UsuarioResponse> LogIn(LoginRequest loginRequest);

    void activarUsuario(ActivarCuentaRequest activarRequest);

    void recuperarPassword (RecuperarPasswordRequest request);

    Optional<SuccessResponse> resetPassword (ResetPasswordRequest resetPasswordRequest);
}
