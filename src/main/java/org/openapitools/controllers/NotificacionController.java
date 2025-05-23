package org.openapitools.controllers;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.dto.NotificacionDTO.NotificacionRequest;
import org.openapitools.dto.NotificacionDTO.NotificacionResponse;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.dto.UsuarioDTO.UsuarioResponse;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.services.interfaces.NotificacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;

    @PostMapping("/notificaciones")
    public ResponseEntity<SuccessResponse> sendNotification(UsuarioResponse usuario,@Valid @RequestBody NotificacionRequest notificacion) {
        var respuesta = notificacionService.sendNotificacion(usuario.id(), notificacion);
        return respuesta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/notificaciones")
    public ResponseEntity<List<NotificacionResponse>> getNotificaciones(@RequestBody NotificacionRequest notificacion) {
        var respuesta = notificacionService.getNotificaciones(notificacion);
        return respuesta.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/notificaciones/{id}/leer")
    public ResponseEntity<SuccessResponse> markAsView (@PathVariable String id, String idUsuario) {
        try{
            notificacionService.markAsView(id,idUsuario);
        }catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new SuccessResponse("Notificacion Vista"));
    }
}
