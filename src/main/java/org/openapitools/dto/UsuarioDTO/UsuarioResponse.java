package org.openapitools.dto.UsuarioDTO;

import org.openapitools.model.enums.StatusUsuario;


public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        StatusUsuario status
) {
}
