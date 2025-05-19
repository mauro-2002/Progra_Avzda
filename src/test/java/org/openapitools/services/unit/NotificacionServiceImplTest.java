package org.openapitools.services.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openapitools.dto.NotificacionDTO.NotificacionRequest;
import org.openapitools.dto.NotificacionDTO.NotificacionResponse;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.mappers.NotificacionMapper;
import org.openapitools.model.Notificacion;
import org.openapitools.model.UsuarioStandar;
import org.openapitools.model.enums.Rol;
import org.openapitools.model.enums.StatusUsuario;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.implementations.NotificacionServiceImpl;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificacionServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private NotificacionMapper notificacionMapper;

    @InjectMocks
    private NotificacionServiceImpl notificacionServiceImpl;
    private NotificacionRequest notificacionRequest;
    private NotificacionResponse notificacionResponse;
    private UsuarioStandar user;
    private Notificacion notificacion;

    @BeforeEach
    public void setUp() {
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
    }

    @Test
    void testSendNotificacionSuccess() {
        when(notificacionMapper.parseToNotificacion(notificacionRequest)).thenReturn(notificacion);
        when(userRepository.findUserByID(user.getId())).thenReturn(Optional.of(user));

        var result = notificacionServiceImpl.sendNotificacion(user.getId(), notificacionRequest);
        assertNotNull(result);
    }

    @Test
    void testSendNotificacionFailure() {
        when(userRepository.findUserByID(user.getId())).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> notificacionServiceImpl.sendNotificacion(user.getId(), notificacionRequest));
    }

    @Test
    void testGetNotificacionesSuccess() {

    }

    @Test
    void testGetNotificacionesFailure() {

    }

    @Test
    void testMarkAsViewSuccess() {

    }

    @Test
    void testMarkAsViewFailure() {

    }
}
