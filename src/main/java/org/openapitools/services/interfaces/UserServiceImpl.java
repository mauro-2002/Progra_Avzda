package org.openapitools.services.interfaces;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.UserRegistration;
import org.openapitools.model.UserResponse;
import org.openapitools.services.implementations.UserService;
import org.springframework.stereotype.Service;
import org.openapitools.exceptions.UserAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {


}
