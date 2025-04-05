package org.openapitools.dto;

import org.openapitools.model.enums.StatusUsuario;


public record UsuarioResponse(
        String id,
        String nombre,
        String email,
        StatusUsuario estado
) {
}
