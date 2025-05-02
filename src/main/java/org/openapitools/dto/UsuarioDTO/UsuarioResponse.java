package org.openapitools.dto.UsuarioDTO;

import org.openapitools.model.enums.StatusUsuario;


public record UsuarioResponse(
        String id,
        String nombre,
        String email,
        StatusUsuario status
) {
}
