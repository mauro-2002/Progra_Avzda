package org.openapitools.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActivarCuentaRequest(
        @NotBlank(message = "El campo es requerido")
        @Size(min = 6,message = "La longitud mínima es 6")
        String codigo
) {
}
