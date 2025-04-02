package org.openapitools.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ResetPasswordRequest(
        @NotBlank(message = "El campo es requerido")
        @Size(min = 6,message = "La longitud mínima es 6")
        int codigo,
        @NotBlank(message = "El campo es requerido")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$",message = "Debe contener al menos: un número, una letra minúscula y una mayúscula ")
        @Size(min = 8,message = "La longitud mínima es 8")
        String newPassword
) {
}
