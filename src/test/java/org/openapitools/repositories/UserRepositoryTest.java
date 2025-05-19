package org.openapitools.repositories;


import org.openapitools.data.TestDataLoader;
import org.openapitools.dto.UsuarioDTO.UsuarioResponse;
import org.openapitools.model.UsuarioStandar;
import org.openapitools.model.enums.StatusUsuario;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class UserRepositoryTest {

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
    void testFindUserByIDSuccess(){
        var testUser = users.values().stream().findAny().orElseThrow();

        Optional<UsuarioStandar> foundUserByID = userRepository.findById(testUser.getId());

        assertTrue(foundUserByID.isPresent());
        assertEquals(testUser.getId(), foundUserByID.get().getId());
    }

    @Test
    void testFindUserByExistingEmailSuccess() {
        // Sección de Arrange: Obtiene un usuario aleatorio a ser empleado en la prueba
        var testUser = users.values().stream().findAny().orElseThrow();
        // Sección de Act: Se ejecuta la búsqueda de usuario por email.
        Optional<UsuarioStandar> result = userRepository.findExistingUserByEmail(testUser.getEmail());

        // Sección de Assert: Se verifica que el usuario obtenido corresponda con los datos esperados.
        assertTrue(result.isPresent());
        assertEquals(testUser.getEmail(), result.get().getEmail());
    }


    @Test
    void testFindExistingUserByEmailWhenUserDeleted() {
        // Sección de Arrange: Obtiene un usuario aleatorio a ser empleado en la prueba
        var testUser = users.values().stream().findAny().orElseThrow();
        testUser.setStatus(StatusUsuario.ELIMINADO);
        userRepository.save(testUser);
        // Sección de Act: Se ejecuta la búsqueda de usuario por email con estado diferente a DELETED.
        Optional<UsuarioStandar> result = userRepository.findExistingUserByEmail(testUser.getEmail());
        // Sección de Assert: Se verifica que el usuario no sea obtenido porque ha sido borrado.
        assertFalse(result.isPresent());
    }

    @Test
    void testFindByStatusNot() {
        // Sección de Arrange: Se asumen los datos precargados como datos de prueba
        // Sección de Act: Se ejecuta la búsqueda de los usuarios que no tienen status DELETED
        List<UsuarioResponse> usersFound = userRepository.findByStatusNot(StatusUsuario.ELIMINADO);
        // Sección de Assert: Se verifica que se haya encontrado todos usuarios precargados.
        assertFalse(usersFound.isEmpty());
        assertEquals(this.users.size(), usersFound.size());
        assertTrue(this.users.values().stream().map(UsuarioStandar::getId).toList().containsAll(usersFound.stream().map(UsuarioResponse::id).toList()));
    }
}
