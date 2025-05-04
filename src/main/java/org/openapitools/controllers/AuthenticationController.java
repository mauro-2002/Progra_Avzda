package org.openapitools.controllers;

import lombok.RequiredArgsConstructor;
import org.openapitools.dto.AuthDTO.ActivarCuentaRequest;
import org.openapitools.dto.AuthDTO.LoginRequest;
import org.openapitools.dto.AuthDTO.RecuperarPasswordRequest;
import org.openapitools.dto.AuthDTO.ResetPasswordRequest;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.services.interfaces.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/auth/login")
    public ResponseEntity<SuccessResponse> login (@RequestBody LoginRequest loginRequest) {

        return ResponseEntity.ok(new SuccessResponse(""));
    }

    @PostMapping("/auth/activar")
    public ResponseEntity<SuccessResponse> activarCuenta (@RequestBody ActivarCuentaRequest Request) {
        return ResponseEntity.ok(new SuccessResponse(""));
    }

    @PostMapping("/auth/recuperar-password")
    public ResponseEntity<SuccessResponse> recuperarCuenta(@RequestBody RecuperarPasswordRequest passwordRequest) {
        return ResponseEntity.ok(new SuccessResponse(""));
    }

    @PostMapping("/auth/reset-password")
    public ResponseEntity<SuccessResponse> resetPassword(@RequestBody ResetPasswordRequest passwordRequest) {
        return ResponseEntity.ok(new SuccessResponse(""));
    }
}
