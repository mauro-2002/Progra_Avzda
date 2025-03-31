package org.openapitools.services.implementations;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.openapitools.dto.UserDto;
import org.openapitools.model.User;
import org.openapitools.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser (@NotNull UserDto userDto);
    List<User> getAllUsers();
    User getUserById(@NotBlank String id);
    User updateUser(@NotBlank String id, @NotNull User user);
    void deleteUser(@NotBlank String id);

}
