package org.openapitools.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.dto.UsuarioDTO.UsuarioRequest;
import org.openapitools.dto.UsuarioDTO.UsuarioResponse;
import org.openapitools.dto.UsuarioDTO.UsuarioUpdateRequest;
import org.openapitools.exceptions.UserNotFoundException;
import org.openapitools.model.Notificacion;
import org.openapitools.services.interfaces.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;


    @PostMapping("/usuarios")
    public ResponseEntity<UsuarioResponse> addUsuario(@Valid @RequestBody UsuarioRequest usuario) {

        var response = usuarioService.createUsuario(usuario);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> updateUsuario(@PathVariable String id,@Valid @RequestBody UsuarioUpdateRequest usuario) {
        try {
            var response = usuarioService.updateUsuario(id, usuario);
            return ResponseEntity.ok().body(response);
        }catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<SuccessResponse> deleteUsuario(@PathVariable String id) {
        try{
            var response = usuarioService.deleteUsuario(id);
            return ResponseEntity.ok().body(response);
        } catch (UserNotFoundException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuarios/{id}/notificaciones")
    public ResponseEntity<List<Notificacion>> getUsuarioNotificaciones(@PathVariable String id) {
        var response = usuarioService.getNotificacionesUsuario(id);
        return ResponseEntity.ok().body(response);
    }



}
