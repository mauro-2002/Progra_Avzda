package org.openapitools.services.implementations;

import lombok.RequiredArgsConstructor;
import org.openapitools.dto.AuthDTO.ActivarCuentaRequest;
import org.openapitools.dto.AuthDTO.LoginRequest;
import org.openapitools.dto.AuthDTO.RecuperarPasswordRequest;
import org.openapitools.dto.AuthDTO.ResetPasswordRequest;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.dto.UsuarioDTO.UsuarioResponse;
import org.openapitools.mappers.UsuarioMapper;
import org.openapitools.model.UsuarioStandar;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.interfaces.AuthenticationService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    private final UserRepository userRepository;
    private final UsuarioMapper userMapper;


    @Override
    public Optional<UsuarioResponse> LogIn(LoginRequest loginRequest) {
        return Optional.empty();
    }

    @Override
    public Optional<SuccessResponse> activarUsuario(UsuarioStandar usuario, ActivarCuentaRequest activarCuentaRequest) {
        return Optional.empty();
    }

    @Override
    public void recuperarPassword(RecuperarPasswordRequest request) {

    }

    @Override
    public Optional<SuccessResponse> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return Optional.empty();
    }
}
