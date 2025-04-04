package org.openapitools.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.openapitools.model.StatusUsuario;

public record UsuarioResponse(
        @NotBlank(message = "El campo es requerido")
        String nombre,
        @NotBlank(message = "El campo es requerido")
        @Email(message = "Debe ser un email válido")
        String correo,
        StatusUsuario estado
) {
}
