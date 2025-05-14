package org.openapitools.Security;

import org.openapitools.dto.AuthDTO.CustomUserDetails;
import org.openapitools.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


@Configuration
public class UserConfig {

    @Bean
    public UserDetailsService userDetailsServiceFromDataBase(UserRepository userRepository){
        return username -> userRepository.findExistingUserByEmail(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
