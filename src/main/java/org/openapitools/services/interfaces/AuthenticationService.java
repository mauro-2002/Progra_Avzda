package org.openapitools.services.interfaces;

import org.openapitools.dto.AuthDTO.ActivarCuentaRequest;
import org.openapitools.dto.AuthDTO.LoginRequest;
import org.openapitools.dto.AuthDTO.RecuperarPasswordRequest;
import org.openapitools.dto.AuthDTO.ResetPasswordRequest;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.dto.TokenResponse;

import java.util.Optional;

public interface AuthenticationService {

    TokenResponse LogIn(LoginRequest loginRequest);

    Optional<SuccessResponse> activarUsuario(String email, ActivarCuentaRequest activarCuentaRequest);

    void recuperarPassword (RecuperarPasswordRequest request);

    Optional<SuccessResponse> resetPassword (ResetPasswordRequest resetPasswordRequest);
}
