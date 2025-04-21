package org.openapitools.controllers;

import lombok.RequiredArgsConstructor;
import org.openapitools.dto.SuccessResponse;
import org.openapitools.dto.UsuarioRequest;
import org.openapitools.dto.UsuarioResponse;
import org.openapitools.dto.UsuarioUpdateRequest;
import org.openapitools.model.Notificacion;
import org.openapitools.model.Usuario;
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
    public ResponseEntity<UsuarioResponse> addUsuario(@RequestBody UsuarioRequest usuario) {

        var response = usuarioService.createUsuario(usuario);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<UsuarioResponse> updateUsuario(@PathVariable Long id, @RequestBody UsuarioUpdateRequest usuario) {

        var response = usuarioService.updateUsuario(id, usuario);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<SuccessResponse> deleteUsuario(@PathVariable Long id) {
        var response = usuarioService.deleteUsuario(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/usuarios/{id}/notificaciones")
    public ResponseEntity<List<Notificacion>> getUsuarioNotificaciones(@PathVariable Long id) {

        var response = usuarioService.getNotificacionesUsuario(id);
        return ResponseEntity.ok().body(response);
    }
}
