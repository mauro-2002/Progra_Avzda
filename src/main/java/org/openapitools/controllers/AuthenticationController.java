package org.openapitools.controllers;

import lombok.RequiredArgsConstructor;
import org.openapitools.dto.AuthDTO.ActivarCuentaRequest;
import org.openapitools.dto.AuthDTO.LoginRequest;
import org.openapitools.dto.AuthDTO.RecuperarPasswordRequest;
import org.openapitools.dto.AuthDTO.ResetPasswordRequest;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.dto.TokenResponse;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.services.interfaces.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<TokenResponse> login (@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authenticationService.LogIn(loginRequest));
    }

    @PostMapping("/auth/activar")
    public ResponseEntity<SuccessResponse> activarCuenta (@RequestBody ActivarCuentaRequest Request, String email) {
        var response = authenticationService.activarUsuario(email, Request);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.internalServerError().build());
    }

    @PostMapping("/auth/recuperar-password")
    public ResponseEntity<SuccessResponse> recuperarCuenta(@RequestBody RecuperarPasswordRequest passwordRequest) {
        try {
            authenticationService.recuperarPassword(passwordRequest);
            return ResponseEntity.ok(new SuccessResponse("Codigo de recuperacion enviado"));
        }catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<SuccessResponse> resetPassword(@RequestBody ResetPasswordRequest passwordRequest) {
        var response = authenticationService.resetPassword(passwordRequest);
        return response.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.internalServerError().build());
    }
}
