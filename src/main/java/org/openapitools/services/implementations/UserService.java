package org.openapitools.services.implementations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.openapitools.dto.UserDto;
import org.openapitools.model.User;
import org.openapitools.dto.UserResponse;
import org.openapitools.model.Usuario;

import java.util.List;

public interface UserService {

    UserResponse createUser (@NotNull UserDto userDto);
    List<Usuario> getAllUsers();
    Usuario getUserById(@NotBlank String id);
    Usuario updateUser(@NotBlank String id, @NotNull Usuario usuario);
    void deleteUser(@NotBlank String id);

}
