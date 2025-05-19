package org.openapitools.controllers.unit;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.controllers.UsuarioController;
import org.openapitools.dto.UsuarioDTO.UsuarioRequest;
import org.openapitools.dto.UsuarioDTO.UsuarioResponse;
import org.openapitools.dto.UsuarioDTO.UsuarioUpdateRequest;
import org.openapitools.exceptions.UserAlreadyExists;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.services.interfaces.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UsuarioController.class)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private UsuarioService userService;
    private UsuarioRequest request;
    private UsuarioResponse userResponse;
    private UsuarioUpdateRequest userUpdate;
    private UsuarioResponse userUpdateResponse;
    private UsuarioResponse deleteUserResponse;

    @BeforeEach
    void setup() {
        request = new UsuarioRequest("Sebas","sebas@mail.com","Armenia", "calle 20 # 26-22"
        ,"1234567890","0987654321",Rol.STANDAR, StatusUsuario.REGISTRADO);
        userResponse = new UsuarioResponse(UUID.randomUUID().toString(), request.nombre(), request.email(), request.status());
        userUpdate = new UsuarioUpdateRequest("pepe","0987654321", "Bogota", "(ツ)/¯", request.email());
        userUpdateResponse = new UsuarioResponse(userResponse.id(),userUpdate.newNombre(),request.email(),request.status());
        deleteUserResponse = new UsuarioResponse(userResponse.id(),request.nombre(),request.email(),StatusUsuario.ELIMINADO);
    }

    @Test
    void testAddUsuarioSuccess() throws Exception {
        // Sección de Arrange: Se configura la respuesta simulada por el componente userService,
        // en este cado se indica que cuando se envíe la solicitud de creación debe retornar la respuesta dada.
        Mockito.when(userService.createUsuario(Mockito.any(UsuarioRequest.class))).thenReturn(userResponse);
        // Sección de Act: Ejecute la acción de invocación del servicio de registro de usuarios
        mockMvc.perform(post("/usuarios")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                // Sección de Assert: Se verifica que los datos obtenidos correspondan a los del usuario registrado.
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value(request.nombre()))
                .andExpect(jsonPath("$.email").value(request.email()))
                .andExpect(jsonPath("$.rol").value(request.rol().toString()));
    }

    @Test
    void testAddUsuarioThrowsUserAlreadyExistsWhenEmailExists() throws Exception {
        // Sección de Arrange: Se configura la respuesta simulada por el componente userService,
        // en este cado se indica que cuando se envíe la solicitud de creación debe generar una excepción de tipo ValueConflictException.
        Mockito.when(userService.createUsuario(Mockito.any(UsuarioRequest.class))).thenThrow(UserAlreadyExists.class);

        // Sección de Act: Ejecute la acción de invocación del servicio de registro de usuarios
        mockMvc.perform(post("/usuarios")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                // Sección de Assert: Se verifica que el resultado obtenido corresponda a lo esperado un status code de conflicto.
                .andExpect(status().isConflict());
    }

    @Test
    void testUpdateUsuarioSuccess() throws Exception {

        // Arrange
        Mockito.when(userService.updateUsuario(Mockito.eq(userResponse.id()), Mockito.any(UsuarioUpdateRequest.class)))
                .thenReturn(userUpdateResponse);

        // Act & Assert
        mockMvc.perform(put("/usuarios/{id}", userResponse.id())
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value(userUpdate.newNombre()))
                .andExpect(jsonPath("$.email").value(userUpdate.email()));
    }

    @Test
    void testUpdateUsuarioThrowsUserNotFound() throws Exception {
        Mockito.when(userService.updateUsuario(Mockito.eq(userResponse.id()), Mockito.any(UsuarioUpdateRequest.class))).thenThrow(UserNotFoundException.class);

        mockMvc.perform(put("/usuarios/{id}", userResponse.id()))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUsuarioSuccess() throws Exception {
        Mockito.when(userService.deleteUsuario(userResponse.id())).thenReturn(deleteUserResponse);

        mockMvc.perform(put("/usuarios/{id}",userResponse.id())
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(deleteUserResponse.email()))
                .andExpect(jsonPath("$.nombre").value(deleteUserResponse.nombre()))
                .andExpect(jsonPath("$.status").value(deleteUserResponse.status().toString()));
    }

    @Test
    void testDeleteUsuarioThrowsUserNotFound() throws Exception {
        Mockito.when(userService.deleteUsuario(Mockito.eq(userResponse.id()))).thenThrow(UserNotFoundException.class);

        mockMvc.perform(put("/usuarios/{id}", userResponse.id()))
                .andExpect(status().isNotFound());
    }

}
