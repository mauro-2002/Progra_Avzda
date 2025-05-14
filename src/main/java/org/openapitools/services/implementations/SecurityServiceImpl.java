package org.openapitools.services.implementations;

import lombok.RequiredArgsConstructor;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.interfaces.SecurityService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service("securityService")
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;

    @Override
    public boolean isCurrentUser(String id) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findById(id)
                .map(user -> user.getEmail().equals(username))
                .orElse(false);
    }
}
