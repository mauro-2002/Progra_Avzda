package org.openapitools.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RecuperarPasswordRequest(
        @NotBlank(message = "El campo es requerido")
        @Email(message = "Debe ser un email válido")
        String correo
) {
}
