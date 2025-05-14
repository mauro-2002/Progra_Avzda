package org.openapitools.Setup;

import lombok.RequiredArgsConstructor;
import org.openapitools.model.UsuarioStandar;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
public class DefaultUserInitializer implements CommandLineRunner {
    private final DefaultUserProperties defaultUserProperties;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            defaultUserProperties.getUsers().stream()
                    .map(this::createUser).forEach(userRepository::save);
        }
    }

    private UsuarioStandar createUser(DefaultUserProperties.DefaultUser defaultUser){
        return UsuarioStandar.builder()
                .id(UUID.randomUUID().toString())
                .email(defaultUser.username())
                .rol(defaultUser.role())
                .ciudad("Armenia")
                .direccion("Sabra dios")
                .codigoActivacion("")
                .expiracionCodigo(null)
                .password(passwordEncoder.encode(defaultUser.password()))
                .status(StatusUsuario.ACTIVO)
                .build()
                ;

    }

}
