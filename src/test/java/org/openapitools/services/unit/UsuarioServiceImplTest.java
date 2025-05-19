package org.openapitools.services.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.dto.UsuarioDTO.UsuarioRequest;
import org.openapitools.dto.UsuarioDTO.UsuarioResponse;
import org.openapitools.dto.UsuarioDTO.UsuarioUpdateRequest;
import org.openapitools.exceptions.UserAlreadyExists;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.mappers.UsuarioMapper;
import org.openapitools.model.UsuarioStandar;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.implementations.UsuarioServiceImpl;


import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UserRepository userRepository; // Simula la capa de persistencia

    @Mock
    private UsuarioMapper userMapper; // Simula la conversión de entidades a DTOs

    @InjectMocks
    private UsuarioServiceImpl userService; // Inyecta los mocks en la implementación real
    private UsuarioRequest userRequest;
    private UsuarioResponse userResponse;
    private UsuarioStandar user;
    private UsuarioUpdateRequest userUpdateRequest;

    @BeforeEach
    void setUp() {
        user = UsuarioStandar.builder()
                .id(UUID.randomUUID().toString())
                .email("juan@mail.com")
                .nombre("Juan")
                .ciudad("Armeina")
                .direccion("Sabra cristo")
                .password("Abc1234567890")
                .telefono("1234567890")
                .rol(Rol.STANDAR)
                .status(StatusUsuario.ACTIVO).build();
        userRequest = new UsuarioRequest(user.getNombre(),user.getEmail(),user.getCiudad(),user.getDireccion(),user.getPassword(),user.getTelefono(),user.getRol(),user.getStatus());
        userResponse = new UsuarioResponse(UUID.randomUUID().toString(),user.getNombre(), user.getEmail(), user.getStatus());
        userUpdateRequest = new UsuarioUpdateRequest("Sebas", "0987654321", "La Tebaida", "Sabra dios",user.getEmail());
    }

    @Test
    void testCreateUserSuccess() {
        // Arrange: Simular que no existe un usuario con el email dado
        when(userRepository.findExistingUserByEmail(userRequest.email())).thenReturn(Optional.empty());
        when(userMapper.parseOf(userRequest)).thenReturn(user); // Simular conversión DTO -> Entity
        when(userRepository.save(any(UsuarioStandar.class))).thenReturn(user); // Simular persistencia
        when(userMapper.toUserResponse(any(UsuarioStandar.class))).thenReturn(userResponse); // Simular conversión Entity -> DTO

        // Act: Llamar al método que se está probando
        UsuarioResponse result = userService.createUsuario(userRequest);

        // Assert: Verificar que los datos devueltos son los esperados
        assertNotNull(result);
        assertEquals(userResponse.id(), result.id());
        assertEquals(userResponse.email(), result.email());

        // Verificar que los mocks fueron llamados correctamente
        verify(userRepository).findExistingUserByEmail(userRequest.email());
        verify(userRepository).save(any(UsuarioStandar.class));
        verify(userMapper).toUserResponse(any(UsuarioStandar.class));
    }

    @Test
    void testCreateUserThrowsUserAlreadyExistsWhenEmailExists() {
        // Arrange: Simular que ya existe un usuario con el mismo email
        when(userRepository.findExistingUserByEmail(userRequest.email())).thenReturn(Optional.of(user));

        // Act & Assert: Verificar que se lanza la excepción cuando el usuario ya existe
        assertThrows(UserAlreadyExists.class, () -> userService.createUsuario(userRequest));

        // Verificar que el método `save` nunca fue llamado
        verify(userRepository, never()).save(any());
    }

    @Test
    void testGetUserSuccess() {
        // Arrange: Simular que el usuario existe
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userMapper.toUserResponse(any(UsuarioStandar.class))).thenReturn(userResponse);


        // Act: Se ejecuta el método para obtener el usuario
        UsuarioResponse result = userService.getUsuario(user.getId());

        // Assert: Se verifica que el usuario obtenido corresponda con el solicitado
        assertEquals(userResponse.id(), result.id());
        // Verificar que los mocks fueron llamados correctamente
        verify(userRepository).findById(user.getId());
        verify(userMapper).toUserResponse(any(UsuarioStandar.class));
    }

    @Test
    void testGetUserNotFound() {
        // Arrange: Simular que el usuario no existe
        when(userRepository.findById("99")).thenReturn(Optional.empty());

        // Assert: Verificar que se lanza la excepción.
        assertThrows(UserNotFoundException.class, () -> userService.getUsuario("99"));

        verify(userRepository).findById("99");
        verify(userMapper, never()).toUserResponse(any());
    }

    @Test
    void testUserUpdateSuccess() {
        // Arrange: Simula que el usuario existe
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UsuarioStandar.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(UsuarioStandar.class))).thenReturn(userResponse);
        // Act: Ejecucion del metodo para actualizar el usuario
        UsuarioResponse response = userService.updateUsuario(user.getId(), userUpdateRequest);
        // Assets: Verificar el cambio realizado en los datos
        assertEquals(user.getEmail(), response.email());
        assertNotEquals(user.getNombre(), response.nombre());

        //Verificar el llamado de los mocks
        verify(userRepository).findById(user.getId());
        verify(userRepository).save(any(UsuarioStandar.class));
        verify(userMapper).toUserResponse(any(UsuarioStandar.class));
    }

    @Test
    void testUserUpdateThrowsUserNotFound() {
        //Simula que no se encontro el usuario
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());
        //Veriica que se haya lanzado la excepcion
        assertThrows(UserNotFoundException.class, () -> userService.updateUsuario(user.getId(), userUpdateRequest));
        //Verifica el uso de los mocks
        verify(userRepository).findById(user.getId());
        verify(userMapper, never()).toUserResponse(any());
    }

    @Test
    void testDeleteUserSuccess() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.save(any(UsuarioStandar.class))).thenReturn(user);
        when(userMapper.toUserResponse(any(UsuarioStandar.class))).thenReturn(userResponse);

        UsuarioResponse response = userService.deleteUsuario(user.getId());

        assertEquals(userResponse.id(), response.id());
        assertEquals(userResponse.email(), response.email());
        assertNotEquals(userResponse.status(), response.status());

        verify(userRepository).findById(user.getId());
        verify(userRepository).save(any(UsuarioStandar.class));
        verify(userMapper).toUserResponse(any(UsuarioStandar.class));
    }
}
