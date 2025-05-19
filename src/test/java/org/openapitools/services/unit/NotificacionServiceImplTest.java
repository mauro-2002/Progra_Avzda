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
import org.openapitools.model.enums.TipoNotificacion;
import org.openapitools.repositories.UserRepository;
import org.openapitools.services.implementations.NotificacionServiceImpl;

import java.time.LocalDateTime;
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

        notificacion = new Notificacion();
        notificacion.setId(UUID.randomUUID().toString());
        notificacion.setFecha(LocalDateTime.now());
        notificacion.setIdUsuario(user.getId());
        notificacion.setTipo(TipoNotificacion.COMENTARIO_NUEVO);
        notificacion.setLeida(false);

        notificacionRequest = new NotificacionRequest(notificacion.getIdUsuario(),notificacion.getIdReporte(),notificacion.getContenido(),notificacion.getTipo());
        notificacionResponse = new NotificacionResponse(notificacion.getContenido(),notificacion.getFecha().toString(), false, notificacion.getTipo());
    }

    @Test
    void testSendNotificacionSuccess() {
        when(notificacionMapper.parseToNotificacion(notificacionRequest)).thenReturn(notificacion);
        when(userRepository.findUserByID(user.getId())).thenReturn(Optional.of(user));

        var result = notificacionServiceImpl.sendNotificacion(user.getId(), notificacionRequest);

        assertNotNull(result);

        verify(notificacionMapper).parseToNotificacion(notificacionRequest);
        verify(userRepository).findUserByID(user.getId());
    }

    @Test
    void testSendNotificacionFailure() {
        when(userRepository.findUserByID(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> notificacionServiceImpl.sendNotificacion(user.getId(), notificacionRequest));

        verify(notificacionMapper, never()).parseToNotificacion(notificacionRequest);
    }

    @Test
    void testGetNotificacionesSuccess() {
        when(userRepository.findUserByID(user.getId())).thenReturn(Optional.of(user));

        var result = notificacionServiceImpl.getNotificaciones(notificacionRequest);

        assertNotNull(result);

        verify(notificacionMapper).toNotificacionResponse(notificacion);
        verify(userRepository).findUserByID(user.getId());
    }

    @Test
    void testGetNotificacionesFailure() {
        when(userRepository.findUserByID(user.getId())).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> notificacionServiceImpl.getNotificaciones(notificacionRequest));

        verify(notificacionMapper, never()).toNotificacionResponse(notificacion);
    }

}
