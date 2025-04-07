package org.openapitools.dto.UsuarioDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioUpdateRequest(
        @NotBlank(message = "El campo es requerido")
        String newNombre,
        @NotBlank(message = "El campo es requerido")
        @Size(min = 10,message = "No debe tener minimo 10 caracteres")
        String newTelefono,
        @NotBlank(message = "El campo es requerido")
        String newCiudad,
        @NotBlank(message = "El campo es requerido")
        String newDireccion
) {
}
