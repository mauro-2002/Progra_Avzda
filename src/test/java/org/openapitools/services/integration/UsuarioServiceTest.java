package org.openapitools.services.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openapitools.data.TestDataLoader;
import org.openapitools.dto.UsuarioDTO.UsuarioRequest;
import org.openapitools.exceptions.UserAlreadyExists;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.model.UsuarioStandar;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UsuarioServiceTest {
    @Autowired
    private UsuarioService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    private Map<String, UsuarioStandar> users;


    @BeforeEach
    void setUp() {
        users = TestDataLoader.loadTestData(userRepository,mongoTemplate);
    }

    @Test
    void testCreateUser() {
        // Sección de Arrange: Se crean los datos del usuario a ser registrado
        var user = new UsuarioRequest("juan","juan@example.com","Armenia",
                "Por ahi", "1234567890","0987654321",Rol.STANDAR, StatusUsuario.REGISTRADO);
        // Sección de Act: Ejecute la acción de crear usuario.
        var newUser = userService.createUsuario(user);
        // Sección de Assert: Se verifica que el usuario se haya registrado con los datos proporcionados.
        assertNotNull(newUser.id());
        assertEquals(user.email(),newUser.email());
        assertEquals(user.nombre(),newUser.nombre());
        assertEquals(user.status(),newUser.status());
    }

    @Test
    void testCreateUserThrowsUserAlreadyExistsWhenEmailExists() {
        // Sección de Arrange: Se crean los datos del usuario a ser registrado (Con el email de un usuario ya existente).
        var userStore = users.values().stream().findAny().orElseThrow();
        var user = new UsuarioRequest("juan",userStore.getEmail(),"Armenia",
                "Por ahi", "1234567890","0987654321",Rol.STANDAR, StatusUsuario.REGISTRADO);
        // Sección de Act y Sección de Assert: Ejecute la acción de crear usuario se verifica que genere una excepción debido al email repetido.
        assertThrows(UserAlreadyExists.class,() -> userService.createUsuario(user) );
    }

    @Test
    void testGetUserSuccess() {
        // Sección de Arrange: Se obtiene aleatoriamente uno de los usuarios registrado para pruebas.
        var userStore = users.values().stream().findAny().orElseThrow();
        // Sección de Act: Ejecute la acción de obtener usuario basado en su Id.
        var foundUser = userService.getUsuario(userStore.getId());
        // Sección de Assert: Se verifica que los datos obtenidos correspondan a los del usuario almacenado.
        assertEquals(userStore.getEmail(),foundUser.email());
        assertEquals(userStore.getNombre(),foundUser.nombre());
        assertEquals(userStore.getStatus(),foundUser.status());
    }

    @Test
    void testGetUserNotFound() {
        // Sección de Arrange: Se crean los datos del usuario a ser registrado (Con el email de un usuario ya existente).
        var id = UUID.randomUUID().toString();
        // Seccion que verifica el correcto lanzamiento de la excepcion
        assertThrows(UserNotFoundException.class, () -> userService.getUsuario(id));
    }

}
