package org.openapitools.services;

import org.openapitools.dto.*;

import java.util.Optional;

public interface UsuarioService {

    UsuarioResponse createUsuario(UsuarioRequest usuarioRequest);

    UsuarioResponse updateUsuario(UsuarioUpdateRequest usuarioUpdateRequest);

    Optional<UsuarioResponse> LogIn(LoginRequest loginRequest);

    void activarUsuario(ActivarCuentaRequest activarRequest);

    void recuperarPassword (RecuperarPasswordRequest request);

    Optional<SuccessResponse> resetPassword (ResetPasswordRequest resetPasswordRequest);
}
