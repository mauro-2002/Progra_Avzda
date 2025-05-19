package org.openapitools.services.implementations;

import lombok.RequiredArgsConstructor;
import org.openapitools.Security.JwtTokenProvider;
import org.openapitools.dto.AuthDTO.ActivarCuentaRequest;
import org.openapitools.dto.AuthDTO.LoginRequest;
import org.openapitools.dto.AuthDTO.RecuperarPasswordRequest;
import org.openapitools.dto.AuthDTO.ResetPasswordRequest;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.dto.TokenResponse;
import org.openapitools.exceptions.CodeExpiredException;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.EmailService;
import org.openapitools.services.interfaces.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final List<String> correosRecuperacion;

    private final UserRepository userRepository;
    private final EmailService emailService;

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.expiry}")
    private long expiry;

    @Override
    public TokenResponse LogIn(LoginRequest loginRequest) {
        final var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );
        final var roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        final var now = Instant.now();
        final var expire = now.plus(expiry, ChronoUnit.MINUTES);
        return new TokenResponse(jwtTokenProvider.generateTokenAsString(authentication.getName(),roles,now,expire),
                "Bearer",expire,roles);
    }

    @Override
    public Optional<SuccessResponse> activarUsuario(String email, ActivarCuentaRequest activarCuentaRequest) {
        var user = userRepository.findExistingUserByEmail(email);
        if (user.isEmpty()) {
            throw new UserNotFoundException("El usuario no existe");
        }

        if (user.get().getExpiracionCodigo().isBefore(LocalDateTime.now())){
            throw new CodeExpiredException("Su codigo de activacion ya expiro.");
        }

        if (user.get().getCodigoActivacion().equals(activarCuentaRequest.codigo())){
            user.get().setCodigoActivacion("");
            user.get().setExpiracionCodigo(null);
            userRepository.save(user.get());
            return Optional.of(new SuccessResponse("Cuenta activada correctamente"));
        }

        return Optional.empty();
    }

    @Override
    public void recuperarPassword(RecuperarPasswordRequest request) {
        var user = userRepository.findExistingUserByEmail(request.email());
        if (user.isEmpty()) {
            throw new UserNotFoundException("El usuario no existe");
        }
        String codigo = generarCodigo();
        emailService.enviarCorreo(request.email(),"Codigo Recuperacion",
                "Use este codigo para resetear la contraseña de su usuario:\n" +
                        codigo);
        correosRecuperacion.add(request.email());
        user.get().setCodigoRecuperacion(codigo);
        userRepository.save(user.get());
    }

    @Override
    public Optional<SuccessResponse> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        for (String correo : correosRecuperacion) {
            var user = userRepository.findExistingUserByEmail(correo);
            if (user.isPresent()) {
                if (user.get().getCodigoRecuperacion().equals(resetPasswordRequest.codigo())) {
                    user.get().setCodigoRecuperacion("");
                    user.get().setPassword(resetPasswordRequest.newPassword());
                    userRepository.save(user.get());
                    correosRecuperacion.remove(correo);
                    return Optional.of(new SuccessResponse("Password resetado correctamente"));
                }
            }
        }
        return Optional.empty();
    }


    private String generarCodigo() {
        int tam = 6;
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(tam);
        for (int i = 0; i < tam; i++) {
            int digito = random.nextInt(10); // genera un número entre 0 y 9
            sb.append(digito);
        }
        return sb.toString();
    }
}
