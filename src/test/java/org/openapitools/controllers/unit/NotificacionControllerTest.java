package org.openapitools.controllers.unit;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.controllers.NotificacionController;
import org.openapitools.dto.NotificacionDTO.NotificacionRequest;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.model.enums.TipoNotificacion;
import org.openapitools.services.interfaces.NotificacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NotificacionController.class)
public class NotificacionControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificacionService notificacionService;
    private NotificacionRequest notificacionRequest;

    @BeforeEach
    void setUp() {
        notificacionRequest = new NotificacionRequest(
                UUID.randomUUID().toString(), //id usuario
                UUID.randomUUID().toString(), //id reporte
                "Mensaje de ejemplo",
                TipoNotificacion.COMENTARIO_NUEVO);
    }

    @Test
    void sendNotificacionTest() throws Exception {
        Mockito.when(notificacionService.sendNotificacion(
                notificacionRequest.idUsuario(),
                Mockito.any(NotificacionRequest.class)
                )
        ).thenReturn(Optional.of(new SuccessResponse("Notificacion enviada")));
        mockMvc.perform(post("/notificaciones")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(notificacionRequest)))
                .andExpect(status().isOk());
    }

    @Test
    void sendNotificacionesFailureTest() throws Exception {
        Mockito.when(notificacionService.sendNotificacion(notificacionRequest.idUsuario(),
                Mockito.any(NotificacionRequest.class))).thenThrow(UserNotFoundException.class);
        mockMvc.perform(post("/notificaciones")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(notificacionRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void getNotificacionesTest() throws Exception {
        Mockito.when(notificacionService.getNotificaciones(Mockito.any(NotificacionRequest.class))
        ).thenReturn(Optional.of(new ArrayList<>()));

        mockMvc.perform(get("/notificaciones")
                .contentType("application/json"))
        .andExpect(status().isOk());
    }

    @Test
    void getNotificacionesFailureTest() throws Exception {
        Mockito.when(notificacionService.getNotificaciones(Mockito.any(NotificacionRequest.class)))
                .thenThrow(UserNotFoundException.class);
        mockMvc.perform(post("/notificaciones")
                        .contentType("application/json"))
                .andExpect(status().isNotFound());
    }

}
