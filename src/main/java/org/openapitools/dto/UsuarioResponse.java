package org.openapitools.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.openapitools.model.enums.StatusUsuario;


public record UsuarioResponse(
        String id,
        String nombre,
        String email,
        StatusUsuario estado
) {
}
