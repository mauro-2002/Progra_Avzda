package org.openapitools.services.interfaces;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.dto.UserRegistration;
import org.openapitools.dto.UserResponse;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.implementations.UserService;
import org.springframework.stereotype.Service;
import org.openapitools.exceptions.UserAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserResponse createUser(UserRegistrationRequest user) {
        if (userRepository.findUserByEmail(user.email()).isPresent()) {
            throw new ValueConflictException("Email ya registrado");
        }
        var newUser = userMapper.parseOf(user);
        return userMapper.toUserResponse(userRepository.save(newUser));
    }
    @Override
    public Optional<UserResponse> getUser(String id) {
        return userRepository.findById(id)
                .map(userMapper::toUserResponse);

    }
// . . .
}


}
