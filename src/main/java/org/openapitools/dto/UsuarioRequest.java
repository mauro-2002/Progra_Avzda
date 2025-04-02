package org.openapitools.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.openapitools.model.Rol;
import org.openapitools.model.StatusUsuario;

import java.util.Objects;

public record UsuarioRequest(
        @NotBlank(message = "El campo es requerido")
        String nombre,
        @NotBlank(message = "El campo es requerido")
        @Email(message = "Debe ser un email válido")
        String correo,
        String ciudad,
        String direccion,
        @NotBlank(message = "El campo es requerido")
        @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$",message = "Debe contener al menos: un número, una letra minúscula y una mayúscula ")
        @Size(min = 8,message = "La longitud mínima es 8")
        String password,
        @NotBlank(message = "El campo es requerido")
        @Size(min = 10,message = "No debe tener minimo 10 caracteres")
        String telefono,
        Rol rol,
        StatusUsuario status) {

    public UsuarioRequest {
        rol = Objects.requireNonNullElse(rol,Rol.STANDAR);
        status = Objects.requireNonNullElse(status,StatusUsuario.REGISTRADO);
    }
}
