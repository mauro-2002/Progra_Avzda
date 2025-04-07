package org.openapitools.dto.UsuarioDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.openapitools.model.enums.StatusUsuario;

public record UsuarioResponse(
        @NotBlank(message = "El campo es requerido")
        String nombre,
        @NotBlank(message = "El campo es requerido")
        @Email(message = "Debe ser un email válido")
        String correo,
        StatusUsuario estado
) {
}
